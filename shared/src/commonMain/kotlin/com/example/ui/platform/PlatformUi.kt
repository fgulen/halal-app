package com.example.ui.platform

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

// Platforma özel UI yetenekleri. Android: Settings/Build/LocalContext; iOS: UIAccessibility/UIDevice.

/** Kullanıcı hareketleri azalttıysa (Android: animasyon ölçeği 0; iOS: Reduce Motion) true. */
@Composable
expect fun rememberReducedMotion(): Boolean

/** Platform dinamik renk şeması. Desteklenmiyorsa (ya da kapalıysa) null; çağıran marka renklerini kullanır. */
@Composable
expect fun platformDynamicColorScheme(darkTheme: Boolean): ColorScheme?

/** Tablet düzeni mi? (Android: smallestScreenWidthDp >= 600; iOS: iPad) */
@Composable
expect fun isTabletLayout(): Boolean
