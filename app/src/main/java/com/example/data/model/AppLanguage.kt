package com.example.data.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val flag: String
) {
    EN("en", "English", "🇺🇸"),
    DE("de", "Deutsch", "🇩🇪"),
    FR("fr", "Français", "🇫🇷"),
    TR("tr", "Türkçe", "🇹🇷"),
    AR("ar", "العربية", "🇸🇦")
}
