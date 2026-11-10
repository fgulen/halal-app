package com.example.ui.platform

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.dateWithTimeIntervalSince1970

// Uyarı: Bu dosya yalnızca iOS hedefinde derlenir; CI'da kontrol edilir.
actual fun formatScanDate(epochMillis: Long): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "dd MMM, HH:mm"
    return formatter.stringFromDate(NSDate.dateWithTimeIntervalSince1970(epochMillis / 1000.0))
}
