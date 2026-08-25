package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.ui.components.HalalStatusBadge
import com.example.ui.theme.EmeraldGreenBg
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
import com.example.ui.theme.NaturalTextLight
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmBg
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.SuspiciousAmberBadge
import com.example.ui.theme.SuspiciousAmberBg
import com.example.ui.theme.SuspiciousAmberBorder
import com.example.ui.theme.SuspiciousAmberDark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScanHistoryScreen(
    scanHistory: List<FoodProduct>,
    selectedFilter: HalalStatus?,
    onSelectFilter: (HalalStatus?) -> Unit,
    onProductClick: (FoodProduct) -> Unit,
    onClearHistory: () -> Unit,
    onScanNewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showClearDialog by remember { mutableStateOf(false) }

    // Filter products by search text and status
    // Note: scanHistory is already received ordered from newest to oldest (ORDER BY scannedAt DESC)
    val filteredList = scanHistory.filter { product ->
        val matchesFilter = selectedFilter == null || product.status == selectedFilter
        val matchesSearch = searchQuery.isBlank() ||
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.brand.contains(searchQuery, ignoreCase = true) ||
                product.barcode.contains(searchQuery.trim())
        matchesFilter && matchesSearch
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = {
                Text(
                    text = "Geçmişi Temizle",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = NaturalTextDark
                )
            },
            text = {
                Text(
                    text = "Tüm tarama ve sorgulama geçmişinizi silmek istediğinizden emin misiniz? Bu işlem geri alınamaz.",
                    fontSize = 14.sp,
                    color = NaturalTextMuted,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearHistory()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HaramRed),
                    shape = CircleShape
                ) {
                    Text("Evet, Temizle", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("İptal", color = NaturalTextMuted, fontWeight = FontWeight.SemiBold)
                }
            },
            containerColor = NaturalWarmSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalWarmBg)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "YEREL HAFIZA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimaryDeep,
                        letterSpacing = 1.8.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Tarama Geçmişi",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NaturalTextDark,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = EmeraldGreenBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, HalalGreenBorder)
                        ) {
                            Text(
                                text = "${scanHistory.size} Ürün",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimaryDeep,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                if (scanHistory.isNotEmpty()) {
                    Surface(
                        onClick = { showClearDialog = true },
                        shape = CircleShape,
                        color = HaramRedBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HaramRedBorder),
                        modifier = Modifier.testTag("clear_all_history_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Tümünü Temizle",
                                tint = HaramRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Temizle",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = HaramRed
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("history_search_input"),
                placeholder = {
                    Text(
                        "Geçmişte ürün, marka veya barkod ara...",
                        fontSize = 13.sp,
                        color = NaturalTextLight
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Ara",
                        tint = NaturalTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Temizle",
                                tint = NaturalTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = NaturalWarmSurface,
                    unfocusedContainerColor = NaturalWarmSurface,
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = NaturalWarmBorder
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Status Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { onSelectFilter(null) },
                    label = { Text("Tümü (${scanHistory.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = NaturalWarmSurface,
                        labelColor = NaturalTextMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = NaturalWarmBorder,
                        selectedBorderColor = EmeraldPrimary,
                        enabled = true,
                        selected = selectedFilter == null
                    )
                )
                FilterChip(
                    selected = selectedFilter == HalalStatus.HELAL,
                    onClick = { onSelectFilter(if (selectedFilter == HalalStatus.HELAL) null else HalalStatus.HELAL) },
                    label = { Text("Helal", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HalalGreenDark,
                        selectedLabelColor = Color.White,
                        containerColor = HalalGreenBg,
                        labelColor = HalalGreenDark
                    )
                )
                FilterChip(
                    selected = selectedFilter == HalalStatus.SUPHELI,
                    onClick = { onSelectFilter(if (selectedFilter == HalalStatus.SUPHELI) null else HalalStatus.SUPHELI) },
                    label = { Text("Şüpheli", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SuspiciousAmber,
                        selectedLabelColor = Color.White,
                        containerColor = SuspiciousAmberBg,
                        labelColor = SuspiciousAmberDark
                    )
                )
                FilterChip(
                    selected = selectedFilter == HalalStatus.HARAM,
                    onClick = { onSelectFilter(if (selectedFilter == HalalStatus.HARAM) null else HalalStatus.HARAM) },
                    label = { Text("Haram", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HaramRed,
                        selectedLabelColor = Color.White,
                        containerColor = HaramRedBg,
                        labelColor = HaramRedDark
                    )
                )
            }
        }

        // List / Empty state
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = NaturalWarmSurface,
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(EmeraldGreenBg, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isNotBlank() || selectedFilter != null) "Aramaya uygun ürün bulunamadı" else "Henüz Kayıtlı Ürün Yok",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NaturalTextDark,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (searchQuery.isNotBlank() || selectedFilter != null) "Filtreleri sıfırlayarak tekrar arayabilirsiniz." else "Okuttuğunuz barkodlar ve ürünlerin helallik durumları cihazınızda en yeniden eskiye doğru burada saklanır.",
                            fontSize = 13.sp,
                            color = NaturalTextMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onScanNewClick,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            shape = CircleShape,
                            modifier = Modifier.testTag("empty_history_scan_button")
                        ) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Yeni Barkod Oku", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("scan_history_list"),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "EN YENİDEN ESKİYE SIRALANDI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NaturalTextMuted,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(
                    items = filteredList,
                    key = { it.barcode + "_" + it.scannedAt }
                ) { product ->
                    HistoryProductCard(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryProductCard(
    product: FoodProduct,
    onClick: () -> Unit
) {
    val (cardBg, cardBorder, iconBoxBg, iconColor, iconVector, titleColor) = when (product.status) {
        HalalStatus.HARAM -> Hexuple(
            HaramRedBg,
            HaramRedBorder,
            HaramRedBadge,
            HaramRed,
            Icons.Default.Warning,
            HaramRedDark
        )
        HalalStatus.HELAL -> Hexuple(
            HalalGreenBg,
            HalalGreenBorder,
            HalalGreenBadge,
            HalalGreen,
            Icons.Default.CheckCircle,
            HalalGreenDark
        )
        HalalStatus.SUPHELI -> Hexuple(
            SuspiciousAmberBg,
            SuspiciousAmberBorder,
            SuspiciousAmberBadge,
            SuspiciousAmber,
            Icons.Default.WarningAmber,
            SuspiciousAmberDark
        )
        HalalStatus.BULUNAMADI -> Hexuple(
            Color(0xFFF8FAFC),
            Color(0xFFE2E8F0),
            Color(0xFFE2E8F0),
            Color(0xFF64748B),
            Icons.Default.HelpOutline,
            NaturalTextDark
        )
    }

    val formattedDate = remember(product.scannedAt) {
        val date = Date(product.scannedAt)
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale("tr"))
        sdf.format(date)
    }

    Surface(
        onClick = onClick,
        color = cardBg,
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("history_card_${product.barcode}")
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon Container
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(iconBoxBg, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Main Product Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    HalalStatusBadge(
                        status = product.status,
                        fontSize = 10.sp,
                        paddingHorizontal = 8.dp,
                        paddingVertical = 2.dp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${product.brand} • ${product.barcode}",
                        fontSize = 11.sp,
                        color = NaturalTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NaturalTextLight
                    )
                }

                // If harmful/suspicious details exist, display preview tag
                if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sakıncalı: " + product.harmfulOrSuspiciousIngredients.first(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (product.status == HalalStatus.HARAM) HaramRedDark else SuspiciousAmberDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else if (!product.halalCertificate.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sertifika: " + product.halalCertificate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HalalGreenDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Navigation Arrow indicating clickable item to open details
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Detayları Gör",
                tint = NaturalTextLight,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

private data class Hexuple<A, B, C, D, E, F>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F
)
