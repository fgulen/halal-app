package com.example.ui.platform

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

// View üzerinden haptik: izin veya başlatma sırası gerektirmez.
@Composable
actual fun rememberAppHaptics(): AppHaptics {
    val view = LocalView.current
    return remember(view) {
        object : AppHaptics {
            override fun impact() {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            }

            override fun success() {
                val constant = if (Build.VERSION.SDK_INT >= 30) {
                    HapticFeedbackConstants.CONFIRM
                } else {
                    HapticFeedbackConstants.LONG_PRESS
                }
                view.performHapticFeedback(constant)
            }
        }
    }
}
