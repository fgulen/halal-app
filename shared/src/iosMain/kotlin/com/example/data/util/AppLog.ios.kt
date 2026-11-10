package com.example.data.util

actual fun logDebug(tag: String, message: String) {
    println("D/$tag: $message")
}

actual fun logWarn(tag: String, message: String) {
    println("W/$tag: $message")
}

actual fun logError(tag: String, message: String) {
    println("E/$tag: $message")
}
