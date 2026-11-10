package com.example.ui.platform

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun rememberReducedMotion(): Boolean = false

@Composable
actual fun platformDynamicColorScheme(darkTheme: Boolean): ColorScheme? = null

@Composable
actual fun isTabletLayout(): Boolean = false
