@file:OptIn(ExperimentalForeignApi::class)

package com.example.data.local

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory

// iOS verileri: uygulamanın Documents klasörü. Android'deki dosya adları aynı (migration'lar ortak).
private fun ensureDirectory(path: String): String {
    NSFileManager.defaultManager.createDirectoryAtPath(
        path,
        withIntermediateDirectories = true,
        attributes = null,
        error = null
    )
    return path
}

private fun documentsDirectory(): String = ensureDirectory("${NSHomeDirectory()}/Documents")

fun createIosAppDatabase(): AppDatabase = Room.databaseBuilder<AppDatabase>(
    name = "${documentsDirectory()}/$APP_DATABASE_NAME"
)
    .setDriver(BundledSQLiteDriver())
    .addMigrations(*APP_DATABASE_MIGRATIONS)
    .fallbackToDestructiveMigration(dropAllTables = true)
    .build()

fun createIosUserPreferences(): UserPreferences {
    val dir = ensureDirectory("${documentsDirectory()}/datastore")
    val store = PreferenceDataStoreFactory.createWithPath(
        produceFile = { "$dir/user_preferences.preferences_pb".toPath() }
    )
    return UserPreferences(store)
}
