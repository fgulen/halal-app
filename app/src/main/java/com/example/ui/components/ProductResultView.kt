package com.example.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.analyzer.HalalAnalyzer
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.data.model.FlaggedIngredient
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.model.ReportEmail
import com.example.ui.theme.EmeraldPrimary
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
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmBg
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.SuspiciousAmberBadge
import com.example.ui.theme.SuspiciousAmberBg
import com.example.ui.theme.SuspiciousAmberBorder
import com.example.ui.theme.SuspiciousAmberDark

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProductResultBottomSheet(
    product: FoodProduct,
    language: AppLanguage,
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
                .padding(bottom = 36.dp)
        ) {
            // Header Top Bar with close button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
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
                        contentDescription = AppStrings.getClose(language),
                        tint = NaturalTextDark
                    )
                }
            }

            // Big Status Result Banner Card
            StatusHeaderCard(product = product, language = language)

            // Short-form of LegalDisclaimerCard below, placed right under the verdict so it's
            // seen even if the user closes the sheet without scrolling to the full disclaimer.
            ShortDisclaimerLine(language = language)

            Spacer(modifier = Modifier.height(14.dp))

            // Product Image Showcase (from Open Food Facts / High Res)
            ProductShowcaseImageCard(
                imageUrl = product.imageUrl,
                productName = product.name,
                status = product.status,
                language = language
            )

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
                Spacer(modifier = Modifier.height(8.dp))

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
                            text = "${AppStrings.getShareBarcodeLabel(language)}: ${product.barcode}",
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

                Spacer(modifier = Modifier.height(18.dp))

                // Specific Sections Based on Status
                when (product.status) {
                    HalalStatus.HELAL -> {
                        HelalSuccessSection(product, language)
                    }
                    HalalStatus.HARAM -> {
                        HaramWarningSection(product, language)
                    }
                    HalalStatus.SUPHELI -> {
                        SuspiciousWarningSection(product, language)
                    }
                    HalalStatus.BULUNAMADI -> {
                        NotFoundSection(product, language)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Important Legal / Fiqh Disclaimer Card
                LegalDisclaimerCard(language = language)

                Spacer(modifier = Modifier.height(22.dp))

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
                            text = AppStrings.getScanAgain(language),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    FilledIconButton(
                        modifier = Modifier.size(52.dp),
                        shape = CircleShape,
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "${AppStrings.getShareSubjectPrefix(language)}: ${product.name}"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "${AppStrings.getShareResultHeader(language)}:\n" +
                                            "${AppStrings.getShareProductLabel(language)}: ${product.name} (${product.brand})\n" +
                                            "${AppStrings.getShareBarcodeLabel(language)}: ${product.barcode}\n" +
                                            "${AppStrings.getShareStatusLabel(language)}: ${AppStrings.getStatusLabel(product.status, language)}\n" +
                                            "${if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) AppStrings.getShareFlaggedIngredientsLabel(language) + ": " + product.harmfulOrSuspiciousIngredients.joinToString(", ") else ""}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, AppStrings.getShareChooserTitle(language)))
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = AppStrings.getShare(language))
                    }

                    FilledIconButton(
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("report_error_button"),
                        shape = CircleShape,
                        onClick = {
                            val reportIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${ReportEmail.SUPPORT_EMAIL}")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(ReportEmail.SUPPORT_EMAIL))
                                putExtra(Intent.EXTRA_SUBJECT, ReportEmail.buildSubject(product, language))
                                putExtra(Intent.EXTRA_TEXT, ReportEmail.buildBody(product, language))
                            }
                            try {
                                context.startActivity(reportIntent)
                            } catch (_: ActivityNotFoundException) {
                                Toast.makeText(
                                    context,
                                    "${AppStrings.getNoEmailAppFound(language)}: ${ReportEmail.SUPPORT_EMAIL}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Report, contentDescription = AppStrings.getReportError(language))
                    }
                }
            }
        }
    }
}

@Composable
fun StatusHeaderCard(product: FoodProduct, language: AppLanguage) {
    val title = AppStrings.getStatusCardTitle(product.status, language)
    // For HARAM/SUPHELI, prefer the ingredient(s) actually flagged on this product over the
    // generic per-status boilerplate (which used to name a fixed example compound like "E471"
    // even on products, e.g. plain cola, where nothing of the sort was found - the real reason
    // was simply "no halal/vegan claim on file"). Falls back to the generic text only when there
    // is no specific ingredient to name (HELAL, BULUNAMADI, or a defensive empty-list case).
    val subtitle = if (
        (product.status == HalalStatus.HARAM || product.status == HalalStatus.SUPHELI) &&
        product.harmfulOrSuspiciousIngredients.isNotEmpty()
    ) {
        "${AppStrings.getSubtitleContainsPrefix(language)}: ${product.harmfulOrSuspiciousIngredients.joinToString(", ")}"
    } else {
        AppStrings.getStatusCardSubtitle(product.status, language)
    }

    val (cardBg, iconVector) = when (product.status) {
        HalalStatus.HELAL -> Pair(
            Brush.verticalGradient(listOf(Color(0xFF0F8A5F), Color(0xFF065F46))),
            Icons.Default.Check
        )
        HalalStatus.HARAM -> Pair(
            Brush.verticalGradient(listOf(Color(0xFFDC2626), Color(0xFF991B1B))),
            Icons.Default.Dangerous
        )
        HalalStatus.SUPHELI -> Pair(
            Brush.verticalGradient(listOf(Color(0xFFD97706), Color(0xFF92400E))),
            Icons.Default.Warning
        )
        HalalStatus.BULUNAMADI -> Pair(
            Brush.verticalGradient(listOf(Color(0xFF64748B), Color(0xFF334155))),
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
                .padding(vertical = 14.dp, horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Status Emblem Icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
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
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.6.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
fun ShortDisclaimerLine(language: AppLanguage) {
    Text(
        text = when (language) {
            AppLanguage.EN -> "Not a religious ruling - based on Open Food Facts data."
            AppLanguage.DE -> "Kein religiöses Urteil - basiert auf Open Food Facts Daten."
            AppLanguage.FR -> "Pas une fatwa - basé sur les données Open Food Facts."
            AppLanguage.TR -> "Bu bir fetva değildir; sonuçlar Open Food Facts verisine dayanır."
            AppLanguage.AR -> "هذه ليست فتوى - تعتمد النتائج على بيانات Open Food Facts."
        },
        color = NaturalTextMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HelalSuccessSection(product: FoodProduct, language: AppLanguage) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Halal Certificate Card if present
        product.halalCertificate?.let { cert ->
            Surface(
                color = HalalGreenBg,
                shape = RoundedCornerShape(18.dp),
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
                            text = when (language) {
                                AppLanguage.EN -> "Halal / Vegan Verification"
                                AppLanguage.DE -> "Halal / Vegan Zertifizierung"
                                AppLanguage.FR -> "Garantie Halal / Végan"
                                AppLanguage.TR -> "Helal / Bitkisel Güvence"
                                AppLanguage.AR -> "توثيق الحلال والنباتي"
                            },
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
                color = NaturalWarmSurface,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppStrings.getAnalysisReport(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NaturalTextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.reasonOrDetails,
                        fontSize = 13.sp,
                        color = NaturalTextMuted,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // All Ingredients list
        if (product.allIngredients.isNotEmpty()) {
            IngredientsListCard(allIngredients = product.allIngredients, language = language)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HaramWarningSection(product: FoodProduct, language: AppLanguage) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Flagged Prohibited Ingredients with Short Reason
        Surface(
            color = HaramRedBg,
            shape = RoundedCornerShape(18.dp),
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
                        text = AppStrings.getProhibitedIngredientsHeader(language),
                        color = HaramRedDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // If flaggedDetails is available, display name + short reason
                if (product.flaggedDetails.isNotEmpty()) {
                    product.flaggedDetails.forEach { flagged ->
                        FlaggedProblematicItemCard(flagged = flagged, isHaram = true, language = language)
                    }
                } else if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) {
                    product.harmfulOrSuspiciousIngredients.forEach { itemText ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, HaramRedBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(HaramRed, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = itemText,
                                    color = HaramRedDark,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Analysis details
        if (product.reasonOrDetails.isNotBlank()) {
            Surface(
                color = NaturalWarmSurface,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppStrings.getAnalysisReport(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NaturalTextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.reasonOrDetails,
                        fontSize = 13.sp,
                        color = NaturalTextMuted,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // All Ingredients List
        if (product.allIngredients.isNotEmpty()) {
            IngredientsListCard(allIngredients = product.allIngredients, language = language)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuspiciousWarningSection(product: FoodProduct, language: AppLanguage) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Flagged Suspicious Ingredients with Short Reason
        Surface(
            color = SuspiciousAmberBg,
            shape = RoundedCornerShape(18.dp),
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
                        text = AppStrings.getSuspiciousIngredientsHeader(language),
                        color = SuspiciousAmberDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (product.flaggedDetails.isNotEmpty()) {
                    product.flaggedDetails.forEach { flagged ->
                        FlaggedProblematicItemCard(flagged = flagged, isHaram = false, language = language)
                    }
                } else {
                    product.harmfulOrSuspiciousIngredients.forEach { itemText ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SuspiciousAmberBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(SuspiciousAmber, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = itemText,
                                    color = SuspiciousAmberDark,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Details
        if (product.reasonOrDetails.isNotBlank()) {
            Surface(
                color = NaturalWarmSurface,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppStrings.getAnalysisReport(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NaturalTextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.reasonOrDetails,
                        fontSize = 13.sp,
                        color = NaturalTextMuted,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // All Ingredients List
        if (product.allIngredients.isNotEmpty()) {
            IngredientsListCard(allIngredients = product.allIngredients, language = language)
        }
    }
}

@Composable
fun FlaggedProblematicItemCard(flagged: FlaggedIngredient, isHaram: Boolean, language: AppLanguage) {
    val borderColor = if (isHaram) HaramRedBorder else SuspiciousAmberBorder
    val titleColor = if (isHaram) HaramRedDark else SuspiciousAmberDark
    val dotColor = if (isHaram) HaramRed else SuspiciousAmber

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(dotColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = flagged.name,
                    color = titleColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                if (flagged.origin != null) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = flagged.origin,
                        color = NaturalTextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Short reason shown beside / underneath the item
            Text(
                text = "${AppStrings.getReasonLabel(language)}: " + flagged.reason,
                color = NaturalTextDark.copy(alpha = 0.85f),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientsListCard(allIngredients: List<String>, language: AppLanguage) {
    Surface(
        color = NaturalWarmSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = NaturalTextDark,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.getIngredientsTitle(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = NaturalTextDark
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                allIngredients.forEach { ingredient ->
                    val (chipBg, chipBorder, chipText) = when (HalalAnalyzer.classifyIngredientToken(ingredient)) {
                        HalalStatus.HARAM -> Triple(HaramRedBg, HaramRedBorder, HaramRedDark)
                        HalalStatus.SUPHELI -> Triple(SuspiciousAmberBg, SuspiciousAmberBorder, SuspiciousAmberDark)
                        else -> Triple(HalalGreenBg, HalalGreenBorder, HalalGreenDark)
                    }
                    Surface(
                        color = chipBg,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, chipBorder.copy(alpha = 0.7f))
                    ) {
                        Text(
                            text = ingredient,
                            color = chipText,
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
fun LegalDisclaimerCard(language: AppLanguage) {
    Surface(
        color = NaturalWarmSurface,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = EmeraldPrimaryDeep,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = when (language) {
                        AppLanguage.EN -> "Important Note & Disclaimer"
                        AppLanguage.DE -> "Wichtiger Hinweis & Disclaimer"
                        AppLanguage.FR -> "Avertissement important"
                        AppLanguage.TR -> "Önemli Fıkhi & Yasal Bilgilendirme"
                        AppLanguage.AR -> "تنويه وإخلاء مسؤولية مهم"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimaryDeep
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = when (language) {
                        AppLanguage.EN -> "This app provides dietary guidance based on Open Food Facts data and recognized Halal standards. It does not issue definitive religious rulings. Unspecified origin additives (e.g. E471) are classified as doubtful, not directly haram."
                        AppLanguage.DE -> "Diese App gibt keine verbindlichen religiösen Urteile ab. Ergebnisse basieren auf Open Food Facts und allgemeinen Halal-Kriterien. Unklare Zusätze (E471) werden als zweifelhaft eingestuft."
                        AppLanguage.FR -> "Cette application ne délivre pas de fatwa religieuse. Les résultats reposent sur Open Food Facts. Les additifs d'origine non précisée (E471) sont classés comme douteux."
                        AppLanguage.TR -> "Bu uygulama kesin dini hüküm/fetva vermez. Sonuçlar ürün içeriği ve helal gıda kriterlerine göre açıklanır. Kaynağı belirsiz katkılar (E471 vb.) doğrudan haram sayılmayıp şüpheli olarak sınıflandırılır."
                        AppLanguage.AR -> "هذا التطبيق لا يقدم فتاوى دينية قطعية. تعتمد النتائج على مكونات المنتج والمعايير العامة. المضافات غير محددة المصدر تصنف كمشبوهة وليست محرمة مباشرة."
                    },
                    fontSize = 11.sp,
                    color = NaturalTextMuted,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun NotFoundSection(product: FoodProduct, language: AppLanguage) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // The specific reason for *this* scan (not found / network error / malformed
        // barcode / found-but-no-ingredients) was previously computed by the repository
        // and analyzer but silently dropped here - every BULUNAMADI result looked
        // identical regardless of cause. Surface it the same way the other three status
        // sections already surface product.reasonOrDetails.
        if (product.reasonOrDetails.isNotBlank()) {
            Surface(
                color = NaturalWarmSurface,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppStrings.getAnalysisReport(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NaturalTextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.reasonOrDetails,
                        fontSize = 13.sp,
                        color = NaturalTextMuted,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Surface(
            color = NaturalWarmSurface,
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = when (language) {
                        AppLanguage.EN -> "How to Verify Packaging Label?"
                        AppLanguage.DE -> "Wie Sie das Etikett selbst prüfen:"
                        AppLanguage.FR -> "Comment vérifier l'emballage?"
                        AppLanguage.TR -> "Ambalajı Nasıl Kontrol Edebilirsiniz?"
                        AppLanguage.AR -> "كيف تفحص ملصق المكونات؟"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = NaturalTextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (language) {
                        AppLanguage.EN -> "1. Inspect the 'Ingredients' section on the package.\n2. Look for E471, E441 (Gelatin), E120 (Carmine), or L-Cysteine.\n3. Check for Halal / Vegan / Kosher certification marks.\n4. Consult our E-Codes Guide tab to verify any unclear additive."
                        AppLanguage.DE -> "1. Zutatenliste auf der Rückseite prüfen.\n2. Nach E441 (Gelatine), E120 (Karmin) oder E471 suchen.\n3. Auf Halal- oder V-Label Vegan-Siegel achten.\n4. E-Nummern im Leitfaden nachschlagen."
                        AppLanguage.FR -> "1. Examinez la liste des ingrédients au dos.\n2. Repérez E441 (Gélatine), E120 (Cochenille) ou E471.\n3. Recherchez un label Halal ou Végan.\n4. Consultez notre guide des codes E."
                        AppLanguage.TR -> "1. Ambalajın arkasındaki 'İçindekiler' bölümünü inceleyin.\n2. E471, E441 (Jelatin), E120 (Karmin) veya L-Sistein olup olmadığına bakın.\n3. Helal, Vegan veya Koşer logolarını arayın.\n4. E-Kodları rehberimizden şüpheli maddeleri kontrol edin."
                        AppLanguage.AR -> "1. راجع قائمة المكونات على الغلاف.\n2. ابحث عن E441 (جيلاتين)، E120 (كارمين) أو E471.\n3. ابحث عن شعار حلال أو نباتي.\n4. استخدم دليل أكواد E في التطبيق."
                    },
                    fontSize = 13.sp,
                    color = NaturalTextMuted,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun ProductShowcaseImageCard(
    imageUrl: String?,
    productName: String,
    status: HalalStatus,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (statusBorder, statusBg) = when (status) {
        HalalStatus.HELAL -> Pair(HalalGreenBorder, HalalGreenBg)
        HalalStatus.HARAM -> Pair(HaramRedBorder, HaramRedBg)
        HalalStatus.SUPHELI -> Pair(SuspiciousAmberBorder, SuspiciousAmberBg)
        HalalStatus.BULUNAMADI -> Pair(NaturalWarmBorder, NaturalWarmSurface)
    }

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .testTag("product_showcase_image_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (!imageUrl.isNullOrBlank()) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .setHeader("User-Agent", "HalalGlobalFoodScanner/1.0 (Android; Linux; OpenFoodFacts-Viewer)")
                        .crossfade(true)
                        .build(),
                    contentDescription = productName,
                    contentScale = ContentScale.Fit,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = EmeraldPrimary,
                                strokeWidth = 2.5.dp
                            )
                        }
                    },
                    error = { errorState ->
                        android.util.Log.w(
                            "ProductShowcaseImage",
                            "Failed to load image for '$productName' from $imageUrl: ${errorState.result.throwable.message}"
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(NaturalWarmBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fastfood,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = productName,
                                fontSize = 12.sp,
                                color = NaturalTextDark,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${AppStrings.getImageLoadError(language)}: ${errorState.result.throwable.message ?: AppStrings.getUnknownError(language)}",
                                fontSize = 9.sp,
                                color = NaturalTextMuted,
                                maxLines = 2,
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                        .testTag("product_async_image")
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(NaturalWarmBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fastfood,
                            contentDescription = null,
                            tint = NaturalTextMuted,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (language) {
                            AppLanguage.EN -> "Product image from Open Food Facts"
                            AppLanguage.DE -> "Produktbild aus Open Food Facts"
                            AppLanguage.FR -> "Image produit Open Food Facts"
                            AppLanguage.TR -> "Open Food Facts Ürün Görseli"
                            AppLanguage.AR -> "صورة المنتج من Open Food Facts"
                        },
                        fontSize = 12.sp,
                        color = NaturalTextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Top-right Source Pill
            Surface(
                color = NaturalWarmBg.copy(alpha = 0.95f),
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(0.8.dp, NaturalWarmBorder),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(EmeraldPrimary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Open Food Facts (DE/EU/US)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextDark
                    )
                }
            }
        }
    }
}
