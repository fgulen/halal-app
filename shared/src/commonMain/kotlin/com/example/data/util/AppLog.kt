package com.example.data.util

// Ortak günlük. Android'de logcat, diğer hedeflerde standart çıktı.
expect fun logDebug(tag: String, message: String)
expect fun logWarn(tag: String, message: String)
