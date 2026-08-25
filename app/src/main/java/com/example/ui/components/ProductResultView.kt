package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.ui.theme.EmeraldGreenBg
import com.example.ui.theme.EmeraldGreenBorder
import com.example.ui.theme.EmeraldGreenContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryDark
import com.example.ui.theme.EmeraldPrimaryDeep
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.HalalGreenBadge
import com.example.ui.theme.HalalGreenBg
import com.example.ui.theme.HalalGreenBorder
import com.example.ui.theme.HalalGreenDark
import com.example.ui.theme.HaramRed
import com.example.ui.theme.HaramRedBadge
import com.example.ui.theme.HaramRedBg
import com.example.ui.theme.HaramRedBorder
import com.example.ui.theme.HaramRedDark
import com.example.ui.theme.NaturalTextDark
import com.example.ui.theme.NaturalTextLight
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmBg
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface
import com.example.ui.theme.NaturalWarmSurfaceVariant
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.SuspiciousAmberBadge
import com.example.ui.theme.SuspiciousAmberBg
import com.example.ui.theme.SuspiciousAmberBorder
import com.example.ui.theme.SuspiciousAmberDark

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProductResultBottomSheet(
    product: FoodProduct,
    onDismiss: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NaturalWarmBg,
        dragHandle = null,
        modifier = modifier.testTag("product_result_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header Top Bar with close button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .testTag("close_result_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = NaturalTextDark
                    )
                }
            }

            // Big Status Result Banner Card
            StatusHeaderCard(product = product)

            Spacer(modifier = Modifier.height(16.dp))

            // Main Details Container
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Product Name & Brand
                Text(
                    text = product.brand.uppercase(),
                    color = NaturalTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product.name,
                    color = NaturalTextDark,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.testTag("result_product_name")
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Barcode Pill & Category
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = NaturalWarmSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "Barkod: ${product.barcode}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NaturalTextMuted,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = NaturalWarmSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
                        shape = CircleShape
                    ) {
                        Text(
                            text = product.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NaturalTextMuted,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Specific Sections Based on Status
                when (product.status) {
                    HalalStatus.HELAL -> {
                        HelalSuccessSection(product)
                    }
                    HalalStatus.HARAM -> {
                        HaramWarningSection(product)
                    }
                    HalalStatus.SUPHELI -> {
                        SuspiciousWarningSection(product)
                    }
                    HalalStatus.BULUNAMADI -> {
                        NotFoundSection(product)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onScanAgain,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("scan_again_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary
                        ),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tekrar Barkod Tara",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Helal Kontrol: ${product.name}"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Helal Kontrol Uygulaması Sonucu:\n" +
                                            "Ürün: ${product.name} (${product.brand})\n" +
                                            "Barkod: ${product.barcode}\n" +
                                            "Durum: ${product.status.label.uppercase()}\n" +
                                            "${if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) "İçerikteki Maddeler: " + product.harmfulOrSuspiciousIngredients.joinToString(", ") else ""}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Sonucu Paylaş"))
                        },
                        modifier = Modifier.height(52.dp),
                        shape = CircleShape
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Paylaş")
                    }
                }
            }
        }
    }
}

@Composable
fun StatusHeaderCard(product: FoodProduct) {
    val (cardBg, iconBg, iconColor, title, subtitle, iconVector) = when (product.status) {
        HalalStatus.HELAL -> Sextuple(
            Brush.verticalGradient(listOf(Color(0xFF0F8A5F), Color(0xFF065F46))),
            HalalGreen,
            Color.White,
            "HELAL ÜRÜN",
            "Güvenle tüketebilirsiniz. İslami şartlara uygundur.",
            Icons.Default.Check
        )
        HalalStatus.HARAM -> Sextuple(
            Brush.verticalGradient(listOf(Color(0xFFDC2626), Color(0xFF991B1B))),
            Color(0xFFEF4444),
            Color.White,
            "HARAM ÜRÜN",
            "Tüketilmesi uygun değildir! Sakıncalı maddeler içerir.",
            Icons.Default.Dangerous
        )
        HalalStatus.SUPHELI -> Sextuple(
            Brush.verticalGradient(listOf(Color(0xFFD97706), Color(0xFF92400E))),
            Color(0xFFF59E0B),
            Color.White,
            "ŞÜPHELİ ÜRÜN",
            "Dikkat! İçeriğindeki katkı maddelerinin kaynağı belirsizdir.",
            Icons.Default.Warning
        )
        HalalStatus.BULUNAMADI -> Sextuple(
            Brush.verticalGradient(listOf(Color(0xFF64748B), Color(0xFF334155))),
            Color(0xFF94A3B8),
            Color.White,
            "KAYIT BULUNAMADI",
            "Bu barkod veritabanımızda henüz kayıtlı değildir.",
            Icons.Default.Info
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(12.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(cardBg)
                .padding(vertical = 24.dp, horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Status Emblem Icon
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = title,
                            tint = when (product.status) {
                                HalalStatus.HELAL -> HalalGreen
                                HalalStatus.HARAM -> HaramRed
                                HalalStatus.SUPHELI -> SuspiciousAmber
                                else -> Color(0xFF64748B)
                            },
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HelalSuccessSection(product: FoodProduct) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Halal Certificate Card if present
        product.halalCertificate?.let { cert ->
            Surface(
                color = HalalGreenBg,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, HalalGreenBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = HalalGreen,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Helal Sertifika Güvencesi",
                            color = HalalGreenDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = cert,
                            color = HalalGreenDark.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Details / Reason
        if (product.reasonOrDetails.isNotBlank()) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Kontrol Raporu",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.reasonOrDetails,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // All Ingredients list (if available)
        if (product.allIngredients.isNotEmpty()) {
            Text(
                text = "İçindekiler Bilgisi",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                product.allIngredients.forEach { ingredient ->
                    Surface(
                        color = HalalGreenBg,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HalalGreenBorder.copy(alpha = 0.7f))
                    ) {
                        Text(
                            text = ingredient,
                            color = HalalGreenDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HaramWarningSection(product: FoodProduct) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // High Alert Box listing harmful ingredients (MANDATORY REQUIREMENT)
        Surface(
            color = HaramRedBg,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, HaramRedBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ReportProblem,
                        contentDescription = null,
                        tint = HaramRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "İçerikteki Sakıncalı / Haram Maddeler",
                        color = HaramRedDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) {
                    product.harmfulOrSuspiciousIngredients.forEach { harmfulItem ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, HaramRedBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(HaramRed, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = harmfulItem,
                                    color = HaramRedDark,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Sakıncalı bileşenler içerir.",
                        color = HaramRedDark,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Details explanation
        if (product.reasonOrDetails.isNotBlank()) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Fıkhi ve Teknik Açıklama",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.reasonOrDetails,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Halal Alternatives
        if (product.alternatives.isNotEmpty()) {
            Surface(
                color = HalalGreenBg,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, HalalGreenBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Helal Alternatif Ürün Tavsiyeleri",
                            color = HalalGreenDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    product.alternatives.forEach { alt ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "✓", color = HalalGreenDark, fontWeight = FontWeight.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = alt, color = HalalGreenDark, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuspiciousWarningSection(product: FoodProduct) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Suspicious ingredients card
        Surface(
            color = SuspiciousAmberBg,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, SuspiciousAmberBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = SuspiciousAmber,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Şüpheli Görülen Katkı Maddeleri",
                        color = SuspiciousAmberDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                product.harmfulOrSuspiciousIngredients.forEach { suspiciousItem ->
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SuspiciousAmberBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(SuspiciousAmber, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = suspiciousItem,
                                color = SuspiciousAmberDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Details
        if (product.reasonOrDetails.isNotBlank()) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Şüphe Sebebi ve Tavsiye",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.reasonOrDetails,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Alternatives
        if (product.alternatives.isNotEmpty()) {
            Surface(
                color = HalalGreenBg,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, HalalGreenBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Güvenli Alternatifler",
                        color = HalalGreenDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    product.alternatives.forEach { alt ->
                        Text(
                            text = "• $alt",
                            color = HalalGreenDark,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotFoundSection(product: FoodProduct) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nasıl Kontrol Edebilirsiniz?",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "1. Ambalajın arkasındaki 'İçindekiler' bölümünü inceleyin.\n" +
                        "2. E471, E441 (Jelatin), E120 (Karmin) veya L-Sistein olup olmadığına bakın.\n" +
                        "3. TSE Helal veya GİMDES Helal logosu arayın.\n" +
                        "4. E-Kodları rehberimizden katkı maddelerinin kaynağını kontrol edin.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

private data class Sextuple<A, B, C, D, E, F>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F
)
