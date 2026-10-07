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
        db.version = 4
        db.close()
    }

    @Test
    fun `upgrading v4 to current keeps scan history and defaults madhhab to UNSPECIFIED`() = runTest {
        createVersion4Database()

        database = AppDatabase.build(context, dbName)
        val history = database!!.productDao().getScanHistory().first()

        assertEquals(1, history.size)
        val row = history.first()
        assertEquals("3017620422003", row.barcode)
        assertEquals(HalalStatus.SUPHELI, row.status)
        assertEquals(Madhhab.UNSPECIFIED, row.madhhab)
    }
}
