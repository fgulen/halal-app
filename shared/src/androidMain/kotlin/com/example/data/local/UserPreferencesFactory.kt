package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import java.io.File
import okio.Path.Companion.toPath

// Dosya yolu eski `preferencesDataStore(name = "user_preferences")` ile BİREBİR aynı:
// filesDir/datastore/user_preferences.preferences_pb. Yol değişirse kullanıcının dili
// ve mezhep ayarı sıfırlanır. Aynı dosyaya ikinci bir DataStore açılmaması için tek örnek tutulur.
private val storeLock = Any()
private var sharedStore: DataStore<Preferences>? = null

fun createUserPreferences(context: Context): UserPreferences {
    val store = synchronized(storeLock) {
        sharedStore ?: PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                File(context.applicationContext.filesDir, "datastore/user_preferences.preferences_pb")
                    .absolutePath
                    .toPath()
            }
        ).also { sharedStore = it }
    }
    return UserPreferences(store)
}
