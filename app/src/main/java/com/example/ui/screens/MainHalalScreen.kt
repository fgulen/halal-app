package com.example.ui.screens

import android.Manifest
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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.ui.camera.CameraScannerView
import com.example.ui.components.EAdditiveSheet
import com.example.ui.components.HalalStatusBadge
import com.example.ui.components.LanguageSelectionDialog
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
    val language by viewModel.selectedLanguage.collectAsState()
    val isScannerOpen by viewModel.isScannerOpen.collectAsState()
    val activeProduct by viewModel.activeProduct.collectAsState()
    val isManualEntryOpen by viewModel.isManualEntryOpen.collectAsState()
    val isEAdditivesOpen by viewModel.isEAdditivesOpen.collectAsState()
    val isLanguageDialogOpen by viewModel.isLanguageDialogOpen.collectAsState()
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

    var homeSearchQuery by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = NaturalWarmBg,
        bottomBar = {
            NaturalBottomNavigation(
                selectedIndex = selectedBottomNavIndex,
                language = language,
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
            ScanHistoryScreen(
                scanHistory = scanHistory,
                selectedFilter = selectedFilter,
                language = language,
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
                // Header with Language Selector & E-Codes info trigger
                item {
                    NaturalAppHeader(
                        language = language,
                        onLanguageClick = { viewModel.openLanguageDialog() },
                        onECodesClick = { viewModel.openEAdditives() }
                    )
                }

                // Direct Fast Search Input Bar on Home Screen
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = NaturalWarmSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedTextField(
                                value = homeSearchQuery,
                                onValueChange = { homeSearchQuery = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("home_search_input"),
                                placeholder = {
                                    Text(
                                        text = when (language) {
                                            AppLanguage.TR -> "Nutella, Haribo veya Barkod yazın..."
                                            AppLanguage.DE -> "Nutella, Haribo oder Barcode eingeben..."
                                            AppLanguage.FR -> "Entrez Nutella, Haribo ou code-barres..."
                                            AppLanguage.AR -> "ابحث عن نوتيلا، هاريبو أو باركود..."
                                            AppLanguage.EN -> "Type Nutella, Haribo or barcode..."
                                        },
                                        fontSize = 13.sp,
                                        color = NaturalTextMuted
                                    )
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = EmeraldPrimary
                                ),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = {
                                    if (homeSearchQuery.isNotBlank()) {
                                        viewModel.onBarcodeScanned(homeSearchQuery)
                                        homeSearchQuery = ""
                                    }
                                })
                            )
                            if (homeSearchQuery.isNotBlank()) {
                                Button(
                                    onClick = {
                                        viewModel.onBarcodeScanned(homeSearchQuery)
                                        homeSearchQuery = ""
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = when (language) {
                                            AppLanguage.TR -> "Bul"
                                            AppLanguage.DE -> "Suchen"
                                            AppLanguage.FR -> "Chercher"
                                            AppLanguage.AR -> "بحث"
                                            AppLanguage.EN -> "Find"
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                if (homeSearchQuery.isNotBlank()) {
                    val matchingProducts = com.example.data.local.InitialData.sampleProducts.filter {
                        val q = homeSearchQuery.trim().lowercase()
                        it.name.lowercase().contains(q) ||
                        it.brand.lowercase().contains(q) ||
                        it.barcode.contains(q) ||
                        it.category.lowercase().contains(q)
                    }
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = NaturalWarmSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder),
                            shadowElevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Surface(
                                    onClick = {
                                        viewModel.onBarcodeScanned(homeSearchQuery.trim())
                                        homeSearchQuery = ""
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    color = EmeraldPrimary.copy(alpha = 0.08f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null,
                                            tint = EmeraldPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = when (language) {
                                                AppLanguage.TR -> "\"${homeSearchQuery.trim()}\" için Canlı Küresel Arama Yap"
                                                AppLanguage.DE -> "Live-Suche nach „${homeSearchQuery.trim()}“"
                                                AppLanguage.FR -> "Recherche mondiale pour « ${homeSearchQuery.trim()} »"
                                                AppLanguage.AR -> "بحث عالمي مباشر عن \"${homeSearchQuery.trim()}\""
                                                AppLanguage.EN -> "Live Global Search for \"${homeSearchQuery.trim()}\""
                                            },
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldPrimary
                                        )
                                    }
                                }
                                    matchingProducts.take(5).forEach { match ->
                                        Surface(
                                            onClick = {
                                                viewModel.onBarcodeScanned(match.barcode)
                                                homeSearchQuery = ""
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color.Transparent,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (!match.imageUrl.isNullOrBlank()) {
                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = Color.White,
                                                        border = androidx.compose.foundation.BorderStroke(0.8.dp, NaturalWarmBorder),
                                                        modifier = Modifier.size(36.dp)
                                                    ) {
                                                        AsyncImage(
                                                            model = ImageRequest.Builder(LocalContext.current)
                                                                .data(match.imageUrl)
                                                                .setHeader("User-Agent", "HalalFoodChecker/1.0 (Android; OpenFoodFacts-Viewer)")
                                                                .crossfade(true)
                                                                .build(),
                                                            contentDescription = match.name,
                                                            contentScale = ContentScale.Fit,
                                                            modifier = Modifier.fillMaxSize().padding(2.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                }
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = match.name,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = NaturalTextDark,
                                                        maxLines = 1
                                                    )
                                                    Text(
                                                        text = "${match.brand} • ${match.barcode}",
                                                        fontSize = 11.sp,
                                                        color = NaturalTextMuted
                                                    )
                                                }
                                                HalalStatusBadge(
                                                    status = match.status,
                                                    fontSize = 9.sp,
                                                    paddingHorizontal = 6.dp,
                                                    paddingVertical = 2.dp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                // Big Hero "Scan Barcode" Button
                item {
                    NaturalHeroScanButton(
                        language = language,
                        onScanClick = onTriggerScanner
                    )
                }

                // Europe & USA Quick Demo Barcode Carousel
                item {
                    QuickDemoBarcodesSection(
                        language = language,
                        onSelectBarcode = { barcode ->
                            viewModel.onBarcodeScanned(barcode)
                        }
                    )
                }

                // Stats in Natural Warm Surface
                item {
                    NaturalStatsCard(
                        scanHistory = scanHistory,
                        language = language
                    )
                }

                // Recent Scans Section Header
                item {
                    NaturalHistoryHeader(
                        historyCount = scanHistory.size,
                        selectedFilter = selectedFilter,
                        language = language,
                        onSelectFilter = { viewModel.setFilter(it) },
                        onClearHistory = { viewModel.clearAllHistory() }
                    )
                }

                // Scan History Items
                val filteredHistory = scanHistory.filter { item ->
                    selectedFilter == null || item.status == selectedFilter
                }

                if (filteredHistory.isEmpty()) {
                    item {
                        NaturalEmptyHistoryCard(
                            language = language,
                            onScanClick = onTriggerScanner
                        )
                    }
                } else {
                    items(filteredHistory, key = { it.barcode + it.scannedAt }) { product ->
                        NaturalHistoryItemCard(
                            product = product,
                            language = language,
                            onClick = { viewModel.selectHistoryItem(product) }
                        )
                    }
                }
            }
        }
    }

    // Language Selection Modal Dialog
    if (isLanguageDialogOpen) {
        LanguageSelectionDialog(
            currentLanguage = language,
            onSelectLanguage = { newLang ->
                viewModel.setLanguage(newLang)
            },
            onDismiss = { viewModel.closeLanguageDialog() }
        )
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
            language = language,
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
            language = language,
            onDismiss = { viewModel.closeManualEntry() },
            onSubmitBarcode = { barcode ->
                viewModel.onBarcodeScanned(barcode)
            }
        )
    }

    // E-Additives Sheet
    if (isEAdditivesOpen) {
        EAdditiveSheet(
            language = language,
            onDismiss = { viewModel.closeEAdditives() }
        )
    }

    // Loading Dialog Indicator
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = NaturalWarmSurface,
                shadowElevation = 10.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, NaturalWarmBorder)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = EmeraldPrimary,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(36.dp)
                    )
                    Column {
                        Text(
                            text = when (language) {
                                AppLanguage.TR -> "Ürün Analiz Ediliyor..."
                                AppLanguage.DE -> "Produkt wird analysiert..."
                                AppLanguage.FR -> "Analyse du produit..."
                                AppLanguage.AR -> "جاري تحليل المنتج..."
                                AppLanguage.EN -> "Analyzing Product..."
                            },
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextDark,
                            fontSize = 15.sp
                        )
                        Text(
                            text = when (language) {
                                AppLanguage.TR -> "Helal ve içerik kontrolü yapılıyor"
                                AppLanguage.DE -> "Halal-Zutaten werden geprüft"
                                AppLanguage.FR -> "Vérification des ingrédients"
                                AppLanguage.AR -> "فحص المكونات الحلال"
                                AppLanguage.EN -> "Verifying Halal compliance"
                            },
                            color = NaturalTextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NaturalAppHeader(
    language: AppLanguage,
    onLanguageClick: () -> Unit,
    onECodesClick: () -> Unit
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
                text = AppStrings.getWelcome(language),
                color = EmeraldPrimaryDeep,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = AppStrings.getAppName(language),
                color = NaturalTextDark,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Language Selection Chip Button
            Surface(
                onClick = onLanguageClick,
                shape = CircleShape,
                color = NaturalWarmSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreenBorder),
                modifier = Modifier.testTag("language_selector_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = language.flag,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = language.code.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldPrimaryDeep
                    )
                }
            }

            // E-Codes Info Button
            Surface(
                onClick = onECodesClick,
                shape = CircleShape,
                color = EmeraldGreenContainer,
                modifier = Modifier
                    .size(42.dp)
                    .testTag("header_ecodes_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "E-Codes",
                        tint = EmeraldPrimaryDeep,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NaturalHeroScanButton(
    language: AppLanguage,
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
            .padding(top = 14.dp, bottom = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
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
                        contentDescription = AppStrings.getScanBarcode(language),
                        tint = Color.White,
                        modifier = Modifier.size(54.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = AppStrings.getScanBarcode(language),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Floating Pill Badge
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
                text = AppStrings.getScanSubtitle(language),
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
    language: AppLanguage,
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
                text = AppStrings.getQuickTestTitle(language),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = NaturalTextMuted
            )
            Text(
                text = AppStrings.getQuickTestSubtitle(language),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldPrimary
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(com.example.data.local.InitialData.sampleProducts) { sample ->
                QuickDemoNaturalCard(
                    title = sample.name,
                    subtitle = "${sample.brand} • ${sample.barcode}",
                    status = sample.status,
                    imageUrl = sample.imageUrl,
                    onClick = { onSelectBarcode(sample.barcode) }
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
    imageUrl: String? = null,
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
        modifier = Modifier.width(185.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HalalStatusBadge(
                    status = status,
                    fontSize = 9.sp,
                    iconSize = 10.dp,
                    paddingHorizontal = 6.dp,
                    paddingVertical = 2.dp
                )

                if (!imageUrl.isNullOrBlank()) {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, borderColor.copy(alpha = 0.5f)),
                        modifier = Modifier.size(36.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .setHeader("User-Agent", "HalalFoodChecker/1.0 (Android; OpenFoodFacts-Viewer)")
                                .crossfade(true)
                                .build(),
                            contentDescription = title,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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
fun NaturalStatsCard(
    scanHistory: List<FoodProduct>,
    language: AppLanguage
) {
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
                label = AppStrings.getTotalScans(language),
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
                label = AppStrings.getHalalProducts(language),
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
                label = AppStrings.getHaramOrDoubtful(language),
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
    language: AppLanguage,
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
                text = AppStrings.getRecentScans(language),
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
                            contentDescription = AppStrings.getClearHistory(language),
                            tint = NaturalTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == null,
                onClick = { onSelectFilter(null) },
                label = { Text("${AppStrings.getAll(language)} ($historyCount)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
                label = { Text(AppStrings.getStatusLabel(HalalStatus.HARAM, language), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
                label = { Text(AppStrings.getStatusLabel(HalalStatus.SUPHELI, language), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
                label = { Text(AppStrings.getStatusLabel(HalalStatus.HELAL, language), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
    language: AppLanguage,
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
            // Product image thumbnail or status icon
            if (!product.imageUrl.isNullOrBlank()) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
                    modifier = Modifier.size(48.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                }
            } else {
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

                when (product.status) {
                    HalalStatus.HARAM -> {
                        Text(
                            text = if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) {
                                "Prohibited: " + product.harmfulOrSuspiciousIngredients.joinToString(", ")
                            } else {
                                product.reasonOrDetails.ifBlank { "Contains non-halal animal or alcohol derivatives." }
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
                                "Doubtful: " + product.harmfulOrSuspiciousIngredients.joinToString(", ")
                            } else {
                                product.reasonOrDetails.ifBlank { "Contains additives of unverified origin." }
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
                            text = product.halalCertificate?.let { "Certification: $it" }
                                ?: product.reasonOrDetails.ifBlank { "No prohibited additives. Safe and verified." },
                            fontSize = 12.sp,
                            color = bodyTextColor,
                            lineHeight = 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    HalalStatus.BULUNAMADI -> {
                        Text(
                            text = "Barcode not found in Open Food Facts.",
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
fun NaturalEmptyHistoryCard(
    language: AppLanguage,
    onScanClick: () -> Unit
) {
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
                text = when (language) {
                    AppLanguage.EN -> "No Scans Yet"
                    AppLanguage.DE -> "Noch keine Scans vorhanden"
                    AppLanguage.FR -> "Aucun scan pour le moment"
                    AppLanguage.TR -> "Henüz sorgulama yapılmadı"
                    AppLanguage.AR -> "لا توجد عمليات مسح حتى الآن"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = NaturalTextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (language) {
                    AppLanguage.EN -> "Tap the big button above or try one of the quick test barcodes from USA & Europe."
                    AppLanguage.DE -> "Tippen Sie auf den Scan-Button oder testen Sie die Demo-Produkte oben."
                    AppLanguage.FR -> "Appuyez sur le bouton ci-dessus ou essayez les produits de démonstration."
                    AppLanguage.TR -> "Kamerayı açarak bir barkod okutun veya yukarıdaki hızlı test ürünlerini deneyin."
                    AppLanguage.AR -> "اضغط على زر المسح أعلاه أو جرب أحد الرموز السريعة."
                },
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
                Text(
                    text = AppStrings.getScanBarcode(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun NaturalBottomNavigation(
    selectedIndex: Int,
    language: AppLanguage,
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
                label = AppStrings.getNavHome(language),
                isSelected = selectedIndex == 0,
                onClick = { onSelectIndex(0) }
            )
            NaturalNavButton(
                icon = Icons.Default.History,
                label = AppStrings.getNavHistory(language),
                isSelected = selectedIndex == 1,
                onClick = { onSelectIndex(1) }
            )
            NaturalNavButton(
                icon = Icons.Default.AutoStories,
                label = AppStrings.getNavECodes(language),
                isSelected = selectedIndex == 2,
                onClick = { onSelectIndex(2) }
            )
            NaturalNavButton(
                icon = Icons.Default.Keyboard,
                label = AppStrings.getNavManual(language),
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
