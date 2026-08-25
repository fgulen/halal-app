package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreenPrimary,
    onPrimary = EmeraldGreenOnPrimary,
    primaryContainer = Color(0xFF065F46),
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = GoldAccent,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF453000),
    onSecondaryContainer = Color(0xFFFDE68A),
    tertiary = HalalGreen,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = Color(0xFFE2E8F0),
    onSurface = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF94A3B8)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = EmeraldGreenOnPrimary,
    primaryContainer = EmeraldGreenContainer,
    onPrimaryContainer = EmeraldGreenOnContainer,
    secondary = SuspiciousAmber,
    onSecondary = Color.White,
    secondaryContainer = SuspiciousAmberBg,
    onSecondaryContainer = SuspiciousAmberDark,
    tertiary = HalalGreenDark,
    background = NaturalWarmBg,
    surface = NaturalWarmSurface,
    surfaceVariant = NaturalWarmSurfaceVariant,
    onBackground = NaturalTextDark,
    onSurface = NaturalTextDark,
    onSurfaceVariant = NaturalTextMuted
)

@Composable
fun HalalKontrolTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our brand colors for cohesive Halal identity
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
