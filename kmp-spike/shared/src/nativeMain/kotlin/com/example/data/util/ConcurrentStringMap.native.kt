package com.example.data.util

// P2 YER TUTUCU: derleme kontrolü için. Thread-safe DEĞİL. P4'te (iOS platform
// katmanı) gerçek kilitli/atomik implementasyonla değiştirilmeli; aksi halde
// analyzer'ın paylaşılan önbelleği eşzamanlı yazımda bozulabilir.
actual fun newConcurrentStringMap(): MutableMap<String, String> = mutableMapOf()
