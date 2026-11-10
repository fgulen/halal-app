package com.example.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

// iOS: kamera izni ilk kamera oturumunda sistem penceresiyle istenir (NSCameraUsageDescription var).
// Bu yüzden durum "izinli" kabul edilir. Reddedilirse önizleme siyah kalır; cihazda doğrulanmalı.
@Composable
actual fun rememberCameraPermission(): CameraPermission = remember {
    object : CameraPermission {
        override val isGranted: Boolean get() = true
        override fun launchRequest() = Unit
    }
}
