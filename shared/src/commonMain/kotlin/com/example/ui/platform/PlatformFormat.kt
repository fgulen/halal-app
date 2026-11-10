package com.example.ui.platform

// Tarama tarihi: "dd MMM, HH:mm" biçimi, cihazın yerel ayarıyla. Android/JVM: SimpleDateFormat;
// iOS: NSDateFormatter.
expect fun formatScanDate(epochMillis: Long): String
