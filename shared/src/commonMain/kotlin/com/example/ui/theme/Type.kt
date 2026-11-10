package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Material 3 type scale on the system font. Headings get tight tracking and body/label text gets
// a little positive tracking so small sizes stay legible. Screens still set explicit sizes for
// some hero text, but Material components (dialogs, chips, nav labels) now pick these up.
val Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = AppTextSize.Display,
        lineHeight = 30.sp,
        letterSpacing = (-0.5).sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = AppTextSize.Title,
        lineHeight = 24.sp,
        letterSpacing = (-0.2).sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = AppTextSize.Subtitle,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = AppTextSize.BodyLarge,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = AppTextSize.Body,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = AppTextSize.Small,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = AppTextSize.Label,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = AppTextSize.Caption,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)

// Named text sizes. Every fontSize in the app points at one of these, so a size change happens in
// one place. Values are the ones the screens already used, rounded to this scale.
object AppTextSize {
    /** Tags, badges, nav labels, small metadata (was 9-11sp). */
    val Caption = 11.sp
    /** Compact labels and chip text (12sp). */
    val Label = 12.sp
    /** Secondary body and helper text (13sp). */
    val Small = 13.sp
    /** Body text (14sp). */
    val Body = 14.sp
    /** Emphasised body and subtitles (15sp). */
    val Subtitle = 15.sp
    /** Primary body text (16sp). */
    val BodyLarge = 16.sp
    /** Section titles (18-19sp). */
    val Title = 18.sp
    /** Stat numbers and large titles (20-22sp). */
    val Headline = 20.sp
    /** Screen title and hero text (24-25sp). */
    val Display = 24.sp
}
