package com.example.ui.platform

import androidx.compose.runtime.Composable

/** Kamera izni durumu ve izin isteği. Android: Accompanist; diğer hedefler: henüz bağlı değil. */
interface CameraPermission {
    val isGranted: Boolean

    fun launchRequest()
}

@Composable
expect fun rememberCameraPermission(): CameraPermission
