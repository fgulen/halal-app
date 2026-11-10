package com.example.ui.platform

import androidx.compose.runtime.Composable

// Yer tutucu: kamera izni bu hedefte henüz bağlı değil; izin verilmemiş sayılır.
@Composable
actual fun rememberCameraPermission(): CameraPermission = object : CameraPermission {
    override val isGranted: Boolean get() = false
    override fun launchRequest() = Unit
}
