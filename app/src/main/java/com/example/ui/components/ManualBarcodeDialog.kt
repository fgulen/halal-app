package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.data.local.InitialData
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.NaturalTextDark
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmBg
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualBarcodeDialog(
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
                    text = "Manuel Barkod Sorgula",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NaturalTextDark
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = NaturalTextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Ürün paketinin üzerindeki 8 veya 13 haneli barkod numarasını girin:",
                fontSize = 13.sp,
                color = NaturalTextMuted
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = barcodeInput,
                onValueChange = { barcodeInput = it.filter { ch -> ch.isDigit() } },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("manual_barcode_text_field"),
                placeholder = { Text("Örn: 8690526055554", color = NaturalTextMuted) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null,
                        tint = EmeraldPrimary
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
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
                enabled = barcodeInput.length >= 4,
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
                Text("Ürünü Sorgula", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Hızlı Test Barkodları:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NaturalTextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            InitialData.sampleProducts.take(4).forEach { sample ->
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
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sample.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextDark
                            )
                            Text(
                                text = "${sample.brand} • ${sample.barcode}",
                                fontSize = 11.sp,
                                color = NaturalTextMuted
                            )
                        }
                        HalalStatusBadge(status = sample.status)
                    }
                }
            }
        }
    }
}

