package com.example.ui.platform

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import platform.UIKit.UIAccessibilityIsReduceMotionEnabled
import platform.UIKit.UIDevice
import platform.UIKit.UIUserInterfaceIdiomPad

// Uyarı: Bu dosya yalnızca iOS hedefinde derlenir; Windows'ta doğrulanamaz, CI'da kontrol edilir.
@Composable
actual fun rememberReducedMotion(): Boolean = UIAccessibilityIsReduceMotionEnabled()

@Composable
actual fun platformDynamicColorScheme(darkTheme: Boolean): ColorScheme? = null

@Composable
actual fun isTabletLayout(): Boolean =
    UIDevice.currentDevice.userInterfaceIdiom == UIUserInterfaceIdiomPad
