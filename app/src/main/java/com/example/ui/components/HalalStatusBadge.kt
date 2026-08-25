package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HalalStatus
import com.example.ui.theme.HalalGreenBadge
import com.example.ui.theme.HalalGreenDark
import com.example.ui.theme.HaramRedBadge
import com.example.ui.theme.HaramRedDark
import com.example.ui.theme.SuspiciousAmberBadge
import com.example.ui.theme.SuspiciousAmberDark

@Composable
fun HalalStatusBadge(
    status: HalalStatus,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 11.sp,
    iconSize: Dp = 13.dp,
    paddingHorizontal: Dp = 8.dp,
    paddingVertical: Dp = 3.dp
) {
    val (bgColor, textColor, icon) = when (status) {
        HalalStatus.HELAL -> Triple(
            HalalGreenBadge,
            HalalGreenDark,
            Icons.Default.CheckCircle
        )
        HalalStatus.HARAM -> Triple(
            HaramRedBadge,
            HaramRedDark,
            Icons.Default.Warning
        )
        HalalStatus.SUPHELI -> Triple(
            SuspiciousAmberBadge,
            SuspiciousAmberDark,
            Icons.Default.WarningAmber
        )
        HalalStatus.BULUNAMADI -> Triple(
            Color(0xFFE2E8F0),
            Color(0xFF475569),
            Icons.Default.HelpOutline
        )
    }

    Box(
        modifier = modifier
            .background(bgColor, CircleShape)
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = status.label,
                tint = textColor,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.label.uppercase(),
                color = textColor,
                fontSize = fontSize,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

