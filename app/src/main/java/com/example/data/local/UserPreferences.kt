package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.AppLanguage
import com.example.data.model.Madhhab
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {

    private val languageKey = stringPreferencesKey("selected_language")

    val selectedLanguage = context.userPreferencesDataStore.data.map { prefs ->
        prefs[languageKey]?.let { code -> AppLanguage.values().find { it.code == code } } ?: AppLanguage.EN
    }

    suspend fun setSelectedLanguage(language: AppLanguage) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[languageKey] = language.code
        }
    }

    private val madhhabKey = stringPreferencesKey("selected_madhhab")

    val selectedMadhhab = context.userPreferencesDataStore.data.map { prefs ->
        prefs[madhhabKey]?.let { code -> Madhhab.values().find { it.code == code } } ?: Madhhab.UNSPECIFIED
    }

    suspend fun setSelectedMadhhab(madhhab: Madhhab) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[madhhabKey] = madhhab.code
        }
    }
}
