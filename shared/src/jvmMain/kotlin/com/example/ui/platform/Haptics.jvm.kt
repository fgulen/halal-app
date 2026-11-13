package com.example.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

// JVM (test/masaüstü) hedefinde dokunsal geri bildirim yok.
@Composable
actual fun rememberAppHaptics(): AppHaptics = remember {
    object : AppHaptics {
        override fun impact() {}
        override fun success() {}
    }
}
