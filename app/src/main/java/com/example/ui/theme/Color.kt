package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Neutral base, Apple-style: a cool, barely-green light grey page with pure white cards.
// The earlier linen/beige page (F3ECE2) looked dirty next to the emerald and the verdict colours.
// Page vs. card contrast comes from the grey step, not from a shadow.
// (Names keep the old "Warm" prefix so the many call sites do not change.)
val NaturalWarmBg = Color(0xFFF2F4F2)
val NaturalWarmSurface = Color(0xFFFFFFFF)
val NaturalWarmSurfaceVariant = Color(0xFFEEF2EF)
val NaturalWarmBorder = Color(0xFFDFE5E1)
// Outline for every dialog and bottom sheet, drawn a step darker than the card border.
val PopupBorder = Color(0xFFCBD5CF)

val NaturalTextDark = Color(0xFF111418)
val NaturalTextMuted = Color(0xFF4B5563) // Crisp readable charcoal-gray
val NaturalTextLight = Color(0xFF6B7280)
val NaturalSearchPlaceholder = Color(0xFF4B5563)

// Emerald Tones
val EmeraldPrimary = Color(0xFF059669)
val EmeraldPrimaryDark = Color(0xFF047857)
val EmeraldPrimaryDeep = Color(0xFF065F46)
val EmeraldGreenOnPrimary = Color(0xFFFFFFFF)
val EmeraldGreenContainer = Color(0xFFD1FAE5)
val EmeraldGreenOnContainer = Color(0xFF065F46)
val EmeraldGreenBg = Color(0xFFECFDF5)
val EmeraldGreenBorder = Color(0xFFA7F3D0)

// Semantic Status Colors (Rose for Haram, Amber for Suspicious, Emerald for Halal)
val HalalGreen = Color(0xFF059669)
val HalalGreenDark = Color(0xFF065F46)
val HalalGreenBg = Color(0xFFECFDF5)
val HalalGreenBorder = Color(0xFFD1FAE5)
val HalalGreenBadge = Color(0xFFA7F3D0)

val HaramRed = Color(0xFFE11D48)
val HaramRedDark = Color(0xFF9F1239)
val HaramRedBg = Color(0xFFFFF1F2)
val HaramRedBorder = Color(0xFFFFE4E6)
val HaramRedBadge = Color(0xFFFECDD3)

val SuspiciousAmber = Color(0xFFD97706)
val SuspiciousAmberDark = Color(0xFF92400E)
val SuspiciousAmberBg = Color(0xFFFFFBEB)
val SuspiciousAmberBorder = Color(0xFFFEF3C7)
val SuspiciousAmberBadge = Color(0xFFFDE68A)

// Neutral slate - for counts that aren't a verdict (e.g. "Total Scans"). Deliberately NOT
// Emerald/Halal green: that tile used the exact same hex as the Halal stat next to it, making
// "every scan I've ever done" visually read as "everything I scanned is halal."
val NeutralSlate = Color(0xFF475569)
val NeutralSlateBg = Color(0xFFF1F5F9)
val NeutralSlateBorder = Color(0xFFE2E8F0)

// Backward compatibility alias
val EmeraldGreenPrimary = EmeraldPrimary
val LightBackground = NaturalWarmBg
val LightSurface = NaturalWarmSurface
val LightSurfaceVariant = NaturalWarmSurfaceVariant
val DarkBackground = Color(0xFF141917)
val DarkSurface = Color(0xFF1C2420)
val DarkSurfaceVariant = Color(0xFF25302B)
val GoldAccent = Color(0xFFD97706)
val GoldAccentLight = Color(0xFFFEF3C7)


// Named one-off values that used to be inline hex literals in screens.
val RatingStar = Color(0xFFF2B01E)
val NeutralSlateSubtle = Color(0xFFF8FAFC)
val NeutralSlateMid = Color(0xFF64748B)
