package com.example.ui.platform

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.data.platform.Sharer

// Paylaşım ve e-posta gerçekleştirmesi kök composable'da sağlanır (Android: MainActivity).
val LocalSharer = staticCompositionLocalOf<Sharer> {
    error("Sharer sağlanmadı: CompositionLocalProvider(LocalSharer provides ...) kullanın")
}
