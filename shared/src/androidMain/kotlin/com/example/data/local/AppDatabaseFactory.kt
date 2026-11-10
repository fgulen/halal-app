package com.example.data.local

import android.content.Context
import androidx.room.Room

// Android'de dosya adı ve migration listesi değişmez: kullanıcının mevcut veritabanı aynen açılır.
// Üretim ve migration testi aynı fabrikayı kullanır, böylece test sadece kayıtlı migration'ları değil
// gerçek yapılandırmayı sınar.
fun createAppDatabase(context: Context, name: String = APP_DATABASE_NAME): AppDatabase =
    Room.databaseBuilder<AppDatabase>(context.applicationContext, name)
        .addMigrations(*APP_DATABASE_MIGRATIONS)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()

private val dbLock = Any()
private var sharedDatabase: AppDatabase? = null

fun getAppDatabase(context: Context): AppDatabase = synchronized(dbLock) {
    sharedDatabase ?: createAppDatabase(context).also { sharedDatabase = it }
}

// Yalnız testler için: bellek içi veritabanı (ana iş parçacığı sorgusuna izin verilir).
fun createInMemoryAppDatabase(context: Context): AppDatabase =
    Room.inMemoryDatabaseBuilder<AppDatabase>(context.applicationContext)
        .allowMainThreadQueries()
        .build()
