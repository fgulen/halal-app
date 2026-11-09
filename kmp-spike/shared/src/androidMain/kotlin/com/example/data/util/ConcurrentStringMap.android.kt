package com.example.data.util

actual fun newConcurrentStringMap(): MutableMap<String, String> =
    java.util.concurrent.ConcurrentHashMap()
