package com.example.ui.screens

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.InitialData
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.ui.camera.CameraScannerView
import com.example.ui.components.EAdditiveSheet
import com.example.ui.components.HalalStatusBadge
import com.example.ui.components.ManualBarcodeDialog
import com.example.ui.components.ProductResultBottomSheet
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
import com.example.ui.viewmodel.HalalScannerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainHalalScreen(
    viewModel: HalalScannerViewModel,
    modifier: Modifier = Modifier
) {
    val isScannerOpen by viewModel.isScannerOpen.collectAsState()
    val activeProduct by viewModel.activeProduct.collectAsState()
    val isManualEntryOpen by viewModel.isManualEntryOpen.collectAsState()
    val isEAdditivesOpen by viewModel.isEAdditivesOpen.collectAsState()
    val scanHistory by viewModel.scanHistory.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    var selectedBottomNavIndex by remember { mutableIntStateOf(0) }

    val onTriggerScanner = {
        if (cameraPermissionState.status.isGranted) {
            viewModel.openScanner()
        } else {
            cameraPermissionState.launchPermissionRequest()
            viewModel.openScanner()
        }
    }

    // Main Scaffold with Natural Tones background & bottom navigation
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = NaturalWarmBg,
        bottomBar = {
            NaturalBottomNavigation(
                selectedIndex = selectedBottomNavIndex,
                onSelectIndex = { index ->
                    when (index) {
                        0 -> selectedBottomNavIndex = 0
                        1 -> selectedBottomNavIndex = 1
                        2 -> viewModel.openEAdditives()
                        3 -> viewModel.openManualEntry()
                    }
                }
            )
        }
    ) { innerPadding ->
        if (selectedBottomNavIndex == 1) {
            // Dedicated Scan History Screen (Sorted newest to oldest)
            ScanHistoryScreen(
                scanHistory = scanHistory,
                selectedFilter = selectedFilter,
                onSelectFilter = { viewModel.setFilter(it) },
                onProductClick = { product -> viewModel.selectHistoryItem(product) },
                onClearHistory = { viewModel.clearAllHistory() },
                onScanNewClick = onTriggerScanner,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Natural Tones Header
                item {
                    NaturalAppHeader(
                        onProfileClick = { viewModel.openEAdditives() }
                    )
                }

                // Hero Circle "Barkod Okut" Button with floating pill badge
                item {
                    NaturalHeroScanButton(
                        onScanClick = onTriggerScanner
                    )
                }

                // Quick Demo Barcode Carousel
                item {
                    QuickDemoBarcodesSection(
                        onSelectBarcode = { barcode ->
                            viewModel.onBarcodeScanned(barcode)
                        }
                    )
                }

                // Stats / Summary in Natural Warm Surface
                item {
                    NaturalStatsCard(scanHistory = scanHistory)
                }

                // Son Sorgulama / Tarama Geçmişi Section Header
                item {
                    NaturalHistoryHeader(
                        historyCount = scanHistory.size,
                        selectedFilter = selectedFilter,
                        onSelectFilter = { viewModel.setFilter(it) },
                        onClearHistory = { viewModel.clearAllHistory() }
                    )
                }

                // Scan History Items with Natural Tones Cards
                val filteredHistory = scanHistory.filter { item ->
                    selectedFilter == null || item.status == selectedFilter
                }

                if (filteredHistory.isEmpty()) {
                    item {
                        NaturalEmptyHistoryCard(
                            onScanClick = onTriggerScanner
                        )
                    }
                } else {
                    items(filteredHistory, key = { it.barcode + it.scannedAt }) { product ->
                        NaturalHistoryItemCard(
                            product = product,
                            onClick = { viewModel.selectHistoryItem(product) }
                        )
                    }
                }
            }
        }
    }

    // Camera Scanner Overlay
    if (isScannerOpen) {
        CameraScannerView(
            onBarcodeScanned = { barcode ->
                viewModel.onBarcodeScanned(barcode)
            },
            onCloseScanner = {
                viewModel.closeScanner()
            },
            onOpenManualEntry = {
                viewModel.closeScanner()
                viewModel.openManualEntry()
            }
        )
    }

    // Product Result Bottom Sheet
    activeProduct?.let { product ->
        ProductResultBottomSheet(
            product = product,
            onDismiss = { viewModel.dismissResult() },
            onScanAgain = {
                viewModel.dismissResult()
                onTriggerScanner()
            }
        )
    }

    // Manual Barcode Entry Sheet
    if (isManualEntryOpen) {
        ManualBarcodeDialog(
            onDismiss = { viewModel.closeManualEntry() },
            onSubmitBarcode = { barcode ->
                viewModel.onBarcodeScanned(barcode)
            }
        )
    }

    // E-Additives Sheet
    if (isEAdditivesOpen) {
        EAdditiveSheet(
            onDismiss = { viewModel.closeEAdditives() }
        )
    }
}

@Composable
fun NaturalAppHeader(
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "HOŞ GELDİNİZ",
                color = EmeraldPrimaryDeep,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Helal Rehberi",
                color = NaturalTextDark,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
        }

        // Profile / Info Circle
        Surface(
            onClick = onProfileClick,
            shape = CircleShape,
            color = EmeraldGreenContainer,
            modifier = Modifier
                .size(48.dp)
                .testTag("header_profile_button")
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Bilgi",
                    tint = EmeraldPrimaryDeep,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun NaturalHeroScanButton(
    onScanClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            // Main Big Circular Button (w-56 h-56 / 210dp)
            Box(
                modifier = Modifier
                    .scale(pulseScale)
                    .size(210.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = CircleShape,
                        ambientColor = Color(0x4D059669),
                        spotColor = Color(0x4D059669)
                    )
                    .background(EmeraldPrimary, CircleShape)
                    .border(8.dp, Color(0xFFF0FDF4), CircleShape)
                    .clip(CircleShape)
                    .clickable(onClick = onScanClick)
                    .testTag("giant_scan_barcode_button"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Barkod Okut",
                        tint = Color.White,
                        modifier = Modifier.size(54.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Barkod Okut",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Floating Pill Badge directly below the button
        Surface(
            shape = CircleShape,
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreenBorder),
            shadowElevation = 6.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 12.dp)
        ) {
            Text(
                text = "Saniyeler İçinde Kontrol Et",
                color = EmeraldPrimaryDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun QuickDemoBarcodesSection(
    onSelectBarcode: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HIZLI TEST BARKODLARI",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = NaturalTextMuted
            )
            Text(
                text = "Tek tıkla dene",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldPrimary
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                QuickDemoNaturalCard(
                    title = "Haribo Jelibon",
                    subtitle = "Domuz Jelatini (E441)",
                    status = HalalStatus.HARAM,
                    onClick = { onSelectBarcode("4001686301265") }
                )
            }
            item {
                QuickDemoNaturalCard(
                    title = "Eti Karam %70",
                    subtitle = "TSE Helal Sertifikalı",
                    status = HalalStatus.HELAL,
                    onClick = { onSelectBarcode("8690526055554") }
                )
            }
            item {
                QuickDemoNaturalCard(
                    title = "Çıtır Cips",
                    subtitle = "E471 Şüpheli Katkı",
                    status = HalalStatus.SUPHELI,
                    onClick = { onSelectBarcode("8690637012345") }
                )
            }
            item {
                QuickDemoNaturalCard(
                    title = "Milka Daim",
                    subtitle = "Likör / Alkol Aroması",
                    status = HalalStatus.HARAM,
                    onClick = { onSelectBarcode("7622210449283") }
                )
            }
            item {
                QuickDemoNaturalCard(
                    title = "Torku Banada",
                    subtitle = "GİMDES Helal Belgeli",
                    status = HalalStatus.HELAL,
                    onClick = { onSelectBarcode("8690637000001") }
                )
            }
        }
    }
}

@Composable
fun QuickDemoNaturalCard(
    title: String,
    subtitle: String,
    status: HalalStatus,
    onClick: () -> Unit
) {
    val (bgColor, borderColor) = when (status) {
        HalalStatus.HELAL -> Pair(HalalGreenBg, HalalGreenBorder)
        HalalStatus.HARAM -> Pair(HaramRedBg, HaramRedBorder)
        else -> Pair(SuspiciousAmberBg, SuspiciousAmberBorder)
    }

    Surface(
        onClick = onClick,
        color = bgColor,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier.width(168.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            HalalStatusBadge(
                status = status,
                fontSize = 9.sp,
                iconSize = 10.dp,
                paddingHorizontal = 6.dp,
                paddingVertical = 2.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = NaturalTextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun NaturalStatsCard(scanHistory: List<FoodProduct>) {
    val halalCount = scanHistory.count { it.status == HalalStatus.HELAL }
    val haramCount = scanHistory.count { it.status == HalalStatus.HARAM }
    val suspiciousCount = scanHistory.count { it.status == HalalStatus.SUPHELI }

    Surface(
        color = NaturalWarmSurface,
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NaturalStatItem(
                label = "Toplam Tarama",
                count = scanHistory.size.toString(),
                color = EmeraldPrimaryDeep
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(NaturalWarmBorder)
            )
            NaturalStatItem(
                label = "Helal Ürün",
                count = halalCount.toString(),
                color = HalalGreenDark
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(NaturalWarmBorder)
            )
            NaturalStatItem(
                label = "Haram / Şüpheli",
                count = (haramCount + suspiciousCount).toString(),
                color = HaramRed
            )
        }
    }
}

@Composable
fun NaturalStatItem(
    label: String,
    count: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = NaturalTextMuted
        )
    }
}

@Composable
fun NaturalHistoryHeader(
    historyCount: Int,
    selectedFilter: HalalStatus?,
    onSelectFilter: (HalalStatus?) -> Unit,
    onClearHistory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SON SORGULAMALAR",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = NaturalTextMuted
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (historyCount > 0) {
                    IconButton(
                        onClick = onClearHistory,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Geçmişi Temizle",
                            tint = NaturalTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter chips with Natural Tones styling
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == null,
                onClick = { onSelectFilter(null) },
                label = { Text("Tümü ($historyCount)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
        }
    }
}

@Composable
fun NaturalHistoryItemCard(
    product: FoodProduct,
    onClick: () -> Unit
) {
    val (cardBg, cardBorder, iconBoxBg, iconColor, iconVector, titleColor, bodyTextColor) = when (product.status) {
        HalalStatus.HARAM -> Septuple(
            HaramRedBg,
            HaramRedBorder,
            HaramRedBadge,
            HaramRed,
            Icons.Default.Warning,
            HaramRedDark,
            HaramRedDark.copy(alpha = 0.9f)
        )
        HalalStatus.HELAL -> Septuple(
            HalalGreenBg,
            HalalGreenBorder,
            HalalGreenBadge,
            HalalGreen,
            Icons.Default.CheckCircle,
            HalalGreenDark,
            HalalGreenDark.copy(alpha = 0.9f)
        )
        HalalStatus.SUPHELI -> Septuple(
            SuspiciousAmberBg,
            SuspiciousAmberBorder,
            SuspiciousAmberBadge,
            SuspiciousAmber,
            Icons.Default.WarningAmber,
            SuspiciousAmberDark,
            SuspiciousAmberDark.copy(alpha = 0.9f)
        )
        HalalStatus.BULUNAMADI -> Septuple(
            Color(0xFFF8FAFC),
            Color(0xFFE2E8F0),
            Color(0xFFE2E8F0),
            Color(0xFF64748B),
            Icons.Default.HelpOutline,
            NaturalTextDark,
            NaturalTextMuted
        )
    }

    Surface(
        onClick = onClick,
        color = cardBg,
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .testTag("history_item_${product.barcode}")
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon rounded container (w-14 h-14 rounded-2xl)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBoxBg, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

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

                Spacer(modifier = Modifier.height(4.dp))

                // Detail line matching Natural Tones style
                when (product.status) {
                    HalalStatus.HARAM -> {
                        Text(
                            text = if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) {
                                "Riskli Maddeler: " + product.harmfulOrSuspiciousIngredients.joinToString(", ")
                            } else {
                                product.reasonOrDetails.ifBlank { "Tüketilmesi uygun olmayan maddeler içerir." }
                            },
                            fontSize = 12.sp,
                            color = bodyTextColor,
                            lineHeight = 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    HalalStatus.SUPHELI -> {
                        Text(
                            text = if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) {
                                "Şüpheli Maddeler: " + product.harmfulOrSuspiciousIngredients.joinToString(", ")
                            } else {
                                product.reasonOrDetails.ifBlank { "Katkı kaynağı belirsizdir." }
                            },
                            fontSize = 12.sp,
                            color = bodyTextColor,
                            lineHeight = 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    HalalStatus.HELAL -> {
                        Text(
                            text = product.halalCertificate?.let { "Sertifika: $it" }
                                ?: product.reasonOrDetails.ifBlank { "Katkı maddesi bulunamadı. Tamamen doğal kaynaklıdır." },
                            fontSize = 12.sp,
                            color = bodyTextColor,
                            lineHeight = 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    HalalStatus.BULUNAMADI -> {
                        Text(
                            text = "Barkod veritabanında henüz kayıtlı değildir.",
                            fontSize = 12.sp,
                            color = bodyTextColor,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NaturalEmptyHistoryCard(onScanClick: () -> Unit) {
    Surface(
        color = NaturalWarmSurface,
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(EmeraldGreenBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Henüz sorgulama yapılmadı",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = NaturalTextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Kamerayı açarak bir ürünün barkodunu okutun veya yukarıdaki hızlı test ürünlerini deneyin.",
                fontSize = 12.sp,
                color = NaturalTextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onScanClick,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = CircleShape
            ) {
                Text("Hemen Tara", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun NaturalBottomNavigation(
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 16.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NaturalNavButton(
                icon = Icons.Default.Home,
                label = "Ana Sayfa",
                isSelected = selectedIndex == 0,
                onClick = { onSelectIndex(0) }
            )
            NaturalNavButton(
                icon = Icons.Default.History,
                label = "Geçmiş",
                isSelected = selectedIndex == 1,
                onClick = { onSelectIndex(1) }
            )
            NaturalNavButton(
                icon = Icons.Default.AutoStories,
                label = "E-Kodları",
                isSelected = selectedIndex == 2,
                onClick = { onSelectIndex(2) }
            )
            NaturalNavButton(
                icon = Icons.Default.Keyboard,
                label = "Barkod Gir",
                isSelected = selectedIndex == 3,
                onClick = { onSelectIndex(3) }
            )
        }
    }
}

@Composable
fun NaturalNavButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) EmeraldPrimary else NaturalTextLight,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) EmeraldPrimary else NaturalTextLight
        )
    }
}

private data class Septuple<A, B, C, D, E, F, G>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F, val seventh: G
)

