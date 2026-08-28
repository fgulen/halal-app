package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ProductEntity::class, ScanHistoryEntity::class],
    // v2: cleared cached rows written before the "Open Food Facts Verified" fabricated
    // certificate was removed (see HalalAnalyzer.kt).
    // v3: added ProductEntity.language and reworked the no-claim/no-flag default from Şüpheli
    // to Helal (see HalalAnalyzer.kt Rule 4) - old rows carry stale verdicts/text either way,
    // so they're cleared rather than silently kept.
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "halal_kontrol_database"
                )
                .addCallback(DatabaseCallback(scope))
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.productDao())
                    }
                }
            }

            suspend fun populateDatabase(dao: ProductDao) {
                if (dao.getProductCount() == 0) {
                    dao.insertProducts(InitialData.sampleProducts)
                }
            }
        }
    }
}
