@file:OptIn(ExperimentalForeignApi::class)

package com.example.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

// Uyarı: Yalnızca iOS hedefinde derlenir; CI'da kontrol edilir.
@Composable
actual fun rememberCameraPermission(): CameraPermission {
    val granted = remember {
        mutableStateOf(AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) == AVAuthorizationStatusAuthorized)
    }
    return remember {
        object : CameraPermission {
            override val isGranted: Boolean get() = granted.value

            override fun launchRequest() {
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { ok ->
                    // Callback arka plan iş parçacığında gelebilir; durum yazımı ana kuyrukta yapılır.
                    dispatch_async(dispatch_get_main_queue()) { granted.value = ok }
                }
            }
        }
    }
}
