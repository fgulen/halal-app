package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import com.example.data.local.InitialData
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.NaturalTextDark
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmBg
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualBarcodeDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitBarcode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var barcodeInput by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NaturalWarmBg,
        modifier = modifier.testTag("manual_barcode_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.getManualBarcodeTitle(language),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NaturalTextDark
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NaturalTextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = when (language) {
                    AppLanguage.EN -> "Enter a barcode (e.g. 4008400404127) or search by product name (e.g. Nutella):"
                    AppLanguage.DE -> "Geben Sie einen Barcode (z.B. 4008400404127) oder Produktnamen (z.B. Nutella) ein:"
                    AppLanguage.FR -> "Entrez un code-barres (ex. 4008400404127) ou le nom du produit (ex. Nutella):"
                    AppLanguage.TR -> "Barkod numarasını (örn. 4008400404127) veya ürün adını (örn. Nutella) girin:"
                    AppLanguage.AR -> "أدخل رقم الباركود (مثل 4008400404127) أو اسم المنتج (مثل نوتيلا):"
                },
                fontSize = 13.sp,
                color = NaturalTextMuted
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = barcodeInput,
                onValueChange = { barcodeInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("manual_barcode_text_field"),
                placeholder = { Text("e.g. 4008400404127 or Nutella", color = NaturalTextMuted) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null,
                        tint = EmeraldPrimary
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (barcodeInput.isNotBlank()) {
                            onSubmitBarcode(barcodeInput)
                            onDismiss()
                        }
                    }
                ),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary,
                    unfocusedContainerColor = NaturalWarmSurface,
                    focusedContainerColor = NaturalWarmSurface,
                    unfocusedBorderColor = NaturalWarmBorder
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (barcodeInput.isNotBlank()) {
                        onSubmitBarcode(barcodeInput)
                        onDismiss()
                    }
                },
                enabled = barcodeInput.trim().length >= 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("manual_barcode_submit_button"),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldPrimary
                )
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (language) {
                        AppLanguage.EN -> "Search & Verify Product"
                        AppLanguage.DE -> "Produkt suchen & prüfen"
                        AppLanguage.FR -> "Rechercher et vérifier"
                        AppLanguage.TR -> "Ürünü Ara ve Doğrula"
                        AppLanguage.AR -> "بحث وفحص المنتج"
                    },
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = AppStrings.getQuickTestTitle(language),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NaturalTextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            val displayProducts = remember(barcodeInput) {
                val q = barcodeInput.trim().lowercase()
                if (q.isBlank()) {
                    InitialData.sampleProducts
                } else {
                    InitialData.sampleProducts.filter {
                        it.name.lowercase().contains(q) ||
                        it.brand.lowercase().contains(q) ||
                        it.barcode.contains(q) ||
                        it.category.lowercase().contains(q)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // If user entered a query, always show a prominent live online lookup option
                if (barcodeInput.trim().isNotBlank()) {
                    Surface(
                        onClick = {
                            onSubmitBarcode(barcodeInput.trim())
                            onDismiss()
                        },
                        color = EmeraldPrimary.copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, EmeraldPrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = when (language) {
                                        AppLanguage.EN -> "Live Global Search for \"${barcodeInput.trim()}\""
                                        AppLanguage.DE -> "Globale Live-Suche nach „${barcodeInput.trim()}“"
                                        AppLanguage.FR -> "Recherche mondiale pour « ${barcodeInput.trim()} »"
                                        AppLanguage.TR -> "Canlı Küresel Arama: \"${barcodeInput.trim()}\""
                                        AppLanguage.AR -> "بحث عالمي مباشر عن \"${barcodeInput.trim()}\""
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                Text(
                                    text = when (language) {
                                        AppLanguage.EN -> "Search billions of products across Open Food Facts"
                                        AppLanguage.DE -> "Über 3 Millionen Produkte weltweit durchsuchen"
                                        AppLanguage.FR -> "Rechercher parmi des millions de produits"
                                        AppLanguage.TR -> "Tüm dünya veri tabanından canlı sorgula"
                                        AppLanguage.AR -> "ابحث في ملايين المنتجات حول العالم"
                                    },
                                    fontSize = 11.sp,
                                    color = NaturalTextMuted
                                )
                            }
                        }
                    }
                }

                displayProducts.forEach { sample ->
                    Surface(
                        onClick = {
                            onSubmitBarcode(sample.barcode)
                            onDismiss()
                        },
                        color = NaturalWarmSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!sample.imageUrl.isNullOrBlank()) {
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(0.8.dp, NaturalWarmBorder),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    AsyncImage(
                                        model = sample.imageUrl,
                                        contentDescription = sample.name,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = sample.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextDark,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${sample.brand} • ${sample.barcode}",
                                    fontSize = 11.sp,
                                    color = NaturalTextMuted
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            HalalStatusBadge(status = sample.status)
                        }
                    }
                }
            }
        }
    }
}
