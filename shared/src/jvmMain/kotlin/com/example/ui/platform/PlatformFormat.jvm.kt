package com.example.ui.platform

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun formatScanDate(epochMillis: Long): String =
    SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(epochMillis))
