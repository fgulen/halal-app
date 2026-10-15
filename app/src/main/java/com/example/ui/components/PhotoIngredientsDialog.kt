package com.example.ui.components

import com.example.ui.theme.PopupBorder
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.analyzer.HalalAnalyzer
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.HalalGreenBg
import com.example.ui.theme.HalalGreenBorder
import com.example.ui.theme.HalalGreenDark
import com.example.ui.theme.HaramRedBg
import com.example.ui.theme.HaramRedBorder
import com.example.ui.theme.HaramRedDark
import com.example.ui.theme.NaturalTextDark
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmBg
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.SuspiciousAmberBg
import com.example.ui.theme.SuspiciousAmberBorder
import com.example.ui.theme.SuspiciousAmberDark

/**
 * Shown right after a photo is captured and OCR'd (see PhotoIngredientCapture.kt). Lets the
 * user fix misread words - or type the ingredients from scratch if OCR found nothing - before
 * it's fed into HalalAnalyzer.analyzeIngredientsText. A wrong word here becomes a wrong
 * halal/haram verdict, so this confirmation step is not optional.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PhotoIngredientsDialog(
    language: AppLanguage,
    // Pre-analysis preview: chips follow the madhhab the analysis is about to run under.
    madhhab: Madhhab,
    initialIngredientsText: String,
    onDismiss: () -> Unit,
    onAnalyze: (productName: String, ingredientsText: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var productName by remember { mutableStateOf("") }
    var ingredientsText by remember { mutableStateOf(initialIngredientsText) }
    val looksLikeNutritionTable by remember {
        derivedStateOf { HalalAnalyzer.looksLikeNutritionTableText(ingredientsText) }
    }
    // Live preview of how each item would be classified, so the user sees what's flaggable
    // while still reviewing/fixing the OCR text - before spending an "Analyze" tap to find out.
    // Suppressed for nutrition-table-like text: classifying stray numbers/units as "Helal" would
    // read as a false all-clear for text the warning above already says wasn't even checked.
    val previewTokens by remember {
        derivedStateOf {
            if (HalalAnalyzer.looksLikeNutritionTableText(ingredientsText)) {
                emptyList()
            } else {
                ingredientsText.split(Regex("[,;•\n]")).map { it.trim() }.filter { it.length > 1 }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NaturalWarmBg,
        modifier = modifier
            .border(1.dp, PopupBorder, BottomSheetDefaults.ExpandedShape)
            .testTag("photo_ingredients_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.getPhotoAnalysisDialogTitle(language),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NaturalTextDark
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = AppStrings.getClose(language),
                        tint = NaturalTextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = AppStrings.getPhotoAnalysisDialogSubtitle(language),
                fontSize = 13.sp,
                color = NaturalTextMuted
            )

            if (initialIngredientsText.isBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    color = SuspiciousAmberBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SuspiciousAmberBorder),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = SuspiciousAmber,
                            modifier = Modifier.testTag("photo_ocr_empty_warning_icon")
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = AppStrings.getPhotoAnalysisEmptyOcrWarning(language),
                            fontSize = 12.sp,
                            color = SuspiciousAmberDark,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("photo_analysis_product_name_field"),
                label = { Text(AppStrings.getPhotoAnalysisProductNameHint(language)) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = photoDialogFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = ingredientsText,
                onValueChange = { ingredientsText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .testTag("photo_analysis_ingredients_field"),
                textStyle = TextStyle(color = NaturalTextDark, fontSize = 14.sp),
                label = { Text(AppStrings.getPhotoAnalysisIngredientsHint(language)) },
                shape = RoundedCornerShape(16.dp),
                colors = photoDialogFieldColors()
            )

            if (previewTokens.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = AppStrings.getPhotoAnalysisIngredientsPreviewLabel(language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("photo_analysis_ingredients_preview")
                ) {
                    previewTokens.forEach { token ->
                        val (chipBg, chipBorder, chipText) = when (HalalAnalyzer.classifyIngredientToken(token, madhhab)) {
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
                                text = token,
                                color = chipText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            if (looksLikeNutritionTable) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = SuspiciousAmberBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SuspiciousAmberBorder),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = SuspiciousAmber,
                            modifier = Modifier.testTag("photo_nutrition_table_warning_icon")
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = AppStrings.getPhotoAnalysisNutritionTableWarning(language),
                            fontSize = 12.sp,
                            color = SuspiciousAmberDark,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onAnalyze(productName.trim(), ingredientsText.trim()) },
                enabled = ingredientsText.trim().isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("photo_analysis_submit_button"),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Icon(imageVector = Icons.Default.DocumentScanner, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.getPhotoAnalysisAnalyzeButton(language),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun photoDialogFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = NaturalTextDark,
    unfocusedTextColor = NaturalTextDark,
    focusedBorderColor = EmeraldPrimary,
    focusedLabelColor = EmeraldPrimary,
    unfocusedContainerColor = NaturalWarmSurface,
    focusedContainerColor = NaturalWarmSurface,
    unfocusedBorderColor = NaturalWarmBorder,
    cursorColor = EmeraldPrimary
)
