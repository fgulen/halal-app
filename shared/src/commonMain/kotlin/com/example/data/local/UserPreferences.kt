package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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

    // In-app review prompt: how many scans gave a real verdict, and the scan count at which the
    // review flow was last requested (0 = never), so each threshold fires at most once.
    private val successfulScansKey = intPreferencesKey("successful_scans")
    private val reviewRequestedAtKey = intPreferencesKey("review_requested_at_scans")

    suspend fun recordSuccessfulScan() {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[successfulScansKey] = (prefs[successfulScansKey] ?: 0) + 1
        }
    }

    /**
     * Returns true (and remembers it) if the scan count has reached a review threshold that
     * hasn't been used yet. Google still decides whether the dialog actually appears (quota).
     */
    suspend fun claimReviewPrompt(thresholds: List<Int>): Boolean {
        var claimed = false
        context.userPreferencesDataStore.edit { prefs ->
            val scans = prefs[successfulScansKey] ?: 0
            val requestedAt = prefs[reviewRequestedAtKey] ?: 0
            if (thresholds.any { scans >= it && requestedAt < it }) {
                prefs[reviewRequestedAtKey] = scans
                claimed = true
            }
        }
        return claimed
    }
}
