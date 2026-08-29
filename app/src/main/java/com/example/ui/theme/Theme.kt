package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

// Every text size in this app is a hardcoded `fontSize = X.sp` literal (no MaterialTheme.typography
// usage anywhere), so on tablets - which use the same literals as phones - text reads too small for
// the extra viewing distance/screen size. Boosting fontScale on the shared LocalDensity is the only
// lever that reaches all of them without touching ~125 call sites individually. Multiplying (not
// replacing) preserves the user's own OS-level accessibility font-size setting. Gated to
// smallestScreenWidthDp >= 600 (Android's own sw600dp tablet breakpoint) so on phones boost == 1f and
// the resulting Density is bit-identical to before this change.
private const val TABLET_FONT_SCALE_BOOST = 1.15f
private const val TABLET_MIN_SMALLEST_WIDTH_DP = 600

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

    val isTablet = LocalConfiguration.current.smallestScreenWidthDp >= TABLET_MIN_SMALLEST_WIDTH_DP
    val baseDensity = LocalDensity.current
    val boostedDensity = if (isTablet) {
        Density(baseDensity.density, baseDensity.fontScale * TABLET_FONT_SCALE_BOOST)
    } else {
        baseDensity
    }

    CompositionLocalProvider(LocalDensity provides boostedDensity) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
