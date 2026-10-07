package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ProductEntity::class, ScanHistoryEntity::class],
    // v2: cleared cached rows written before the "Open Food Facts Verified" fabricated
    // certificate was removed (see HalalAnalyzer.kt).
    // v3: added ProductEntity.language and reworked the no-claim/no-flag default from Şüpheli
    // to Helal (see HalalAnalyzer.kt Rule 4) - old rows carry stale verdicts/text either way,
    // so they're cleared rather than silently kept.
    // v4: `products` is a resolved-answer cache, not a browsable table - InitialData.sampleProducts
    // is looked up in-memory (ProductRepository) and no longer bulk-inserted into it, since a wrong
    // curated barcode (e.g. a "Milka Daim" entry whose barcode was actually LU "Prince" biscuits'
    // real barcode) would sit there and permanently shadow the real product's live Open Food Facts
    // data for every device that had already cached it. Existing cached rows are cleared here so
    // already-installed devices drop any such stale/wrong rows; scan_history is untouched.
    // v5: added `madhhab` to products and scan_history (default UNSPECIFIED) so each verdict
    // remembers the madhhab it was computed under. Migrated in place - scan_history is kept.
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DELETE FROM products")
            }
        }

        // Adds the column in place rather than clearing anything: unlike 3->4, existing rows are
        // all still valid - they were simply computed under the default (UNSPECIFIED) madhhab.
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE products ADD COLUMN madhhab TEXT NOT NULL DEFAULT 'UNSPECIFIED'")
                db.execSQL("ALTER TABLE scan_history ADD COLUMN madhhab TEXT NOT NULL DEFAULT 'UNSPECIFIED'")
            }
        }

        internal const val DATABASE_NAME = "halal_kontrol_database"

        // The one place the production builder is configured, shared with the migration test so
        // the test can't pass on a migration list production doesn't actually register. Note the
        // destructive fallback: any version step WITHOUT a registered migration drops every table,
        // scan_history included.
        internal fun build(context: Context, name: String = DATABASE_NAME): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, name)
                .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = build(context)
                INSTANCE = instance
                instance
            }
        }
    }
}
