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
    version = 4,
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

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "halal_kontrol_database"
                )
                .addMigrations(MIGRATION_3_4)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
