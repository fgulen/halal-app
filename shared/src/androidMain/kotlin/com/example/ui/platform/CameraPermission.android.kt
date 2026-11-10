package com.example.ui.platform

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun rememberCameraPermission(): CameraPermission {
    val state = rememberPermissionState(permission = Manifest.permission.CAMERA)
    return remember(state) {
        object : CameraPermission {
            override val isGranted: Boolean get() = state.status.isGranted
            override fun launchRequest() = state.launchPermissionRequest()
        }
    }
}
