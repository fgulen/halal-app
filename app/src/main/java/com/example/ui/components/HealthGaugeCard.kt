package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.data.model.FoodProduct
import com.example.ui.theme.NaturalTextDark
import com.example.ui.theme.NaturalTextLight
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface
import kotlin.math.cos
import kotlin.math.sin

// Gauge colours from worst (E, left) to best (A, right). Health colours only; never used for halal.
private val NUTRI_SEGMENT_COLORS = listOf(
    Color(0xFFDC2626), // E
    Color(0xFFF97316), // D
    Color(0xFFEAB308), // C
    Color(0xFF84CC16), // B
    Color(0xFF16A34A)  // A
)

/** Position on the gauge, 0 = E (leftmost) .. 4 = A (rightmost). Null for a missing or unknown grade. */
internal fun nutriScoreSegmentIndex(grade: String?): Int? {
    val normalized = grade?.trim()?.uppercase() ?: return null
    if (normalized.length != 1) return null
    val index = "EDCBA".indexOf(normalized[0])
    return if (index >= 0) index else null
}

@Composable
fun HealthGaugeCard(product: FoodProduct, language: AppLanguage, modifier: Modifier = Modifier) {
    val segment = nutriScoreSegmentIndex(product.nutriScore)
    val grade = product.nutriScore?.trim()?.uppercase().orEmpty()
    val nova = product.novaGroup?.takeIf { it in 1..4 }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("health_gauge_card"),
        shape = RoundedCornerShape(16.dp),
        color = NaturalWarmSurface,
        border = BorderStroke(1.dp, NaturalWarmBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = AppStrings.getHealthGaugeTitle(language),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Plain sentence first, technical code (Nutri-Score E, NOVA 4) small underneath.
            if (segment != null) {
                NutriScoreGauge(segment = segment)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = AppStrings.getNutriScorePlain(language, grade),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    // Dark text: the light yellow/lime segment colours are too faint on white.
                    color = NaturalTextDark,
                    modifier = Modifier.testTag("health_gauge_grade")
                )
                Text(
                    text = "Nutri-Score $grade",
                    fontSize = 11.sp,
                    color = NaturalTextLight
                )
            } else {
                Text(
                    text = AppStrings.getHealthGaugeNoData(language),
                    fontSize = 13.sp,
                    color = NaturalTextMuted
                )
            }

            if (nova != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = AppStrings.getNovaPlain(language, nova),
                    fontSize = 14.sp,
                    color = NaturalTextDark,
                    modifier = Modifier.testTag("health_gauge_nova")
                )
                Text(
                    text = "NOVA $nova",
                    fontSize = 11.sp,
                    color = NaturalTextLight
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = AppStrings.getHealthGaugeNote(language),
                fontSize = 11.sp,
                color = NaturalTextLight
            )
        }
    }
}

// A scale is directional data: keep E on the left and A on the right even in Arabic (RTL), otherwise
// the labels flip while the arc does not and the colours end up under the wrong letter.
@Composable
private fun NutriScoreGauge(segment: Int) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        NutriScoreGaugeContent(segment)
    }
}

@Composable
private fun NutriScoreGaugeContent(segment: Int) {
    // Needle position as a fraction of the half-circle: 0 = far left (E), 1 = far right (A).
    // It points straight at the grade. No swing: an animated needle starts at E and looked like a
    // wrong E grade while it moved.
    val needle = (segment + 0.5f) / 5f

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
    ) {
        val strokePx = 16.dp.toPx()
        // Half-circle that fits the box: the arc's top sits at center.y - radius.
        val radius = minOf(size.width / 2f, size.height) - strokePx
        val center = Offset(size.width / 2f, size.height - strokePx / 2f)
        val arcTopLeft = Offset(center.x - radius, center.y - radius)
        val arcSize = Size(radius * 2f, radius * 2f)

        // Five 36° segments from 180° (left) to 360° (right). The active one stays fully opaque.
        NUTRI_SEGMENT_COLORS.forEachIndexed { index, color ->
            drawArc(
                color = color.copy(alpha = if (index == segment) 1f else 0.35f),
                startAngle = 180f + index * 36f,
                sweepAngle = 36f,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokePx)
            )
        }

        // Needle angle follows the animated fraction (180° at E .. 360° at A).
        val angleRad = Math.toRadians(180.0 + needle * 180.0)
        val tip = Offset(
            x = center.x + (radius * 0.8f) * cos(angleRad).toFloat(),
            y = center.y + (radius * 0.8f) * sin(angleRad).toFloat()
        )
        drawLine(
            color = NaturalTextDark,
            start = center,
            end = tip,
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(color = NaturalTextDark, radius = 6.dp.toPx(), center = center)
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "E", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NUTRI_SEGMENT_COLORS.first())
        Text(text = "A", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NUTRI_SEGMENT_COLORS.last())
    }
}
