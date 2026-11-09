package com.example.data.util

/**
 * Thread-safe String→String önbellek. JVM'de ConcurrentHashMap, iOS'ta ilgili
 * platform implementasyonu eklenecek (P4). Analyzer, eşzamanlı çağrılarda
 * bu haritayı paylaşır; düz HashMap güvenli değil.
 */
expect fun newConcurrentStringMap(): MutableMap<String, String>
