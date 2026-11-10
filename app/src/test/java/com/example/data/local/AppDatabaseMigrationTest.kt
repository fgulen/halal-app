package com.example.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// exportSchema is off, so there's no Room MigrationTestHelper schema to start from. Instead a
// real v4 database file is built from the exact CREATE statements Room generated for v4 (copied
// from AppDatabase_Impl before the v5 entity change), then opened through the production
// builder. Guards the one thing that matters most: with fallbackToDestructiveMigration, a
// version bump without a registered migration silently wipes the user's scan history.
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppDatabaseMigrationTest {

    private val dbName = "migration-test.db"
    private lateinit var context: Context
    private var database: AppDatabase? = null

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase(dbName)
    }

    @After
    fun tearDown() {
        database?.close()
        context.deleteDatabase(dbName)
    }

    private fun createVersion4Database() {
        val file = context.getDatabasePath(dbName)
        file.parentFile?.mkdirs()
        val db = SQLiteDatabase.openOrCreateDatabase(file, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS `products` (`barcode` TEXT NOT NULL, `name` TEXT NOT NULL, `brand` TEXT NOT NULL, `category` TEXT NOT NULL, `status` TEXT NOT NULL, `halalCertificate` TEXT, `harmfulOrSuspiciousIngredients` TEXT NOT NULL, `allIngredients` TEXT NOT NULL, `reasonOrDetails` TEXT NOT NULL, `alternatives` TEXT NOT NULL, `imageUrl` TEXT, `language` TEXT NOT NULL, PRIMARY KEY(`barcode`))")
        db.execSQL("CREATE TABLE IF NOT EXISTS `scan_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `barcode` TEXT NOT NULL, `name` TEXT NOT NULL, `brand` TEXT NOT NULL, `category` TEXT NOT NULL, `status` TEXT NOT NULL, `halalCertificate` TEXT, `harmfulOrSuspiciousIngredients` TEXT NOT NULL, `allIngredients` TEXT NOT NULL, `reasonOrDetails` TEXT NOT NULL, `alternatives` TEXT NOT NULL, `imageUrl` TEXT, `scannedAt` INTEGER NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f8d1bbe73209a520f1bbf4d2b3ef259b')")
        db.execSQL(
            "INSERT INTO scan_history (barcode, name, brand, category, status, halalCertificate, " +
                "harmfulOrSuspiciousIngredients, allIngredients, reasonOrDetails, alternatives, imageUrl, scannedAt) " +
                "VALUES ('3017620422003', 'Nutella', 'Ferrero', 'Spreads', 'SUPHELI', NULL, 'Whey', 'sugar|||whey', 'reason', '', NULL, 1000)"
        )
        db.execSQL(
            "INSERT INTO products (barcode, name, brand, category, status, halalCertificate, " +
                "harmfulOrSuspiciousIngredients, allIngredients, reasonOrDetails, alternatives, imageUrl, language) " +
                "VALUES ('8690000000001', 'Beyaz Peynir', 'Test', 'Cheese', 'HELAL', NULL, '', 'süt|||tuz|||peynir mayası', 'old v4 verdict', '', NULL, 'TR')"
        )
        db.version = 4
        db.close()
    }

    // v5 = v4 + the two ALTERs of MIGRATION_4_5, holding one cached product and one history row.
    private fun createVersion5Database() {
        createVersion4Database()
        val db = SQLiteDatabase.openDatabase(context.getDatabasePath(dbName).path, null, SQLiteDatabase.OPEN_READWRITE)
        db.execSQL("ALTER TABLE products ADD COLUMN madhhab TEXT NOT NULL DEFAULT 'UNSPECIFIED'")
        db.execSQL("ALTER TABLE scan_history ADD COLUMN madhhab TEXT NOT NULL DEFAULT 'UNSPECIFIED'")
        db.execSQL("DELETE FROM products")
        db.execSQL(
            "INSERT INTO products (barcode, name, brand, category, status, halalCertificate, " +
                "harmfulOrSuspiciousIngredients, allIngredients, reasonOrDetails, alternatives, imageUrl, language, madhhab) " +
                "VALUES ('4001686301265', 'Gummibärchen', 'Test', 'Sweets', 'HARAM', NULL, 'E441 Pork Gelatin', 'zucker|||schweinegelatine', 'old English flag text', '', NULL, 'DE', 'UNSPECIFIED')"
        )
        db.execSQL(
            "INSERT INTO products (barcode, name, brand, category, status, halalCertificate, " +
                "harmfulOrSuspiciousIngredients, allIngredients, reasonOrDetails, alternatives, imageUrl, language, madhhab) " +
                "VALUES ('0021000612803', 'Marshmallows', 'Test', 'Sweets', 'HARAM', NULL, 'E441 Pork Gelatin', 'sugar|||pork gelatin', 'English verdict', '', NULL, 'EN', 'UNSPECIFIED')"
        )
        db.version = 5
        db.close()
    }

    // v6 localizes flag cards; a cached DE row would otherwise keep showing "E441 Pork Gelatin".
    // English rows didn't change, so they stay for offline use.
    // v7 keeps the English row (offline use) and adds the health columns, which stay null until refetched.
    @Test
    fun `upgrading v5 to current drops non-English cache rows and keeps history`() = runTest {
        createVersion5Database()

        database = createAppDatabase(context, dbName)

        assertEquals(null, database!!.productDao().getProductByBarcode("4001686301265"))
        // v8 clears the products cache (see MIGRATION_7_8); scan_history is what survives.
        assertEquals(null, database!!.productDao().getProductByBarcode("0021000612803"))
        assertEquals(1, database!!.productDao().getScanHistory().first().size)
    }

    // v5 also changes verdicts (madhhab overrides, Turkish rennet keywords, microbial rennet no
    // longer flagged), so - like every earlier verdict-logic change - the resolved-answer cache is
    // cleared. Otherwise a v4 "Halal" for a Turkish cheese whose rennet was never caught would be
    // served forever under the default madhhab.
    @Test
    fun `upgrading v4 to current clears the products cache but keeps history`() = runTest {
        createVersion4Database()

        database = createAppDatabase(context, dbName)

        assertEquals(null, database!!.productDao().getProductByBarcode("8690000000001"))
        assertEquals(1, database!!.productDao().getScanHistory().first().size)
    }

    @Test
    fun `upgrading v4 to current keeps scan history and defaults madhhab to UNSPECIFIED`() = runTest {
        createVersion4Database()

        database = createAppDatabase(context, dbName)
        val history = database!!.productDao().getScanHistory().first()

        assertEquals(1, history.size)
        val row = history.first()
        assertEquals("3017620422003", row.barcode)
        assertEquals(HalalStatus.SUPHELI, row.status)
        assertEquals(Madhhab.UNSPECIFIED, row.madhhab)
    }

    // v7 adds nutriScore/novaGroup. The products cache is kept (offline use); cached rows have no
    // health data until the next online scan refetches them. History rows keep their data too.
    @Test
    fun `upgrading v6 to v8 adds health columns and keeps history`() = runTest {
        createVersion5Database()
        val db = SQLiteDatabase.openDatabase(context.getDatabasePath(dbName).path, null, SQLiteDatabase.OPEN_READWRITE)
        db.version = 6
        db.close()

        database = createAppDatabase(context, dbName)

        // v8 clears the products cache (see MIGRATION_7_8); scan_history is what survives.
        assertEquals(null, database!!.productDao().getProductByBarcode("0021000612803"))
        val history = database!!.productDao().getScanHistory().first()
        assertEquals(1, history.size)
        assertEquals(null, history.first().nutriScore)
        assertEquals(null, history.first().novaGroup)
    }

    // v8: rennet keyword rules changed, so cached verdicts are cleared once. History is kept.
    @Test
    fun `upgrading v7 to v8 clears the products cache and keeps history`() = runTest {
        createVersion5Database()
        val db = SQLiteDatabase.openDatabase(context.getDatabasePath(dbName).path, null, SQLiteDatabase.OPEN_READWRITE)
        db.version = 6
        db.close()
        // The fixture is at v6 schema; the production builder runs 6->7 and then 7->8.
        database = createAppDatabase(context, dbName)

        assertEquals(null, database!!.productDao().getProductByBarcode("0021000612803"))
        assertEquals(1, database!!.productDao().getScanHistory().first().size)
    }
}
