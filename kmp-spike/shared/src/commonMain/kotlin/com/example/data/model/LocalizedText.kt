package com.example.data.model

data class LocalizedText(
    val en: String,
    val de: String,
    val fr: String,
    val tr: String,
    val ar: String
) {
    fun get(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> en
        AppLanguage.DE -> de
        AppLanguage.FR -> fr
        AppLanguage.TR -> tr
        AppLanguage.AR -> ar
    }
}
