package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.InitialData
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.data.model.EAdditive
import com.example.data.model.HalalStatus
import com.example.ui.theme.EmeraldGreenBg
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryDeep
import com.example.ui.theme.HalalGreenBg
import com.example.ui.theme.HalalGreenBorder
import com.example.ui.theme.HalalGreenDark
import com.example.ui.theme.HaramRed
import com.example.ui.theme.HaramRedBg
import com.example.ui.theme.HaramRedBorder
import com.example.ui.theme.HaramRedDark
import com.example.ui.theme.NaturalSearchPlaceholder
import com.example.ui.theme.NaturalTextDark
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmBg
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.SuspiciousAmberBg
import com.example.ui.theme.SuspiciousAmberBorder
import com.example.ui.theme.SuspiciousAmberDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EAdditiveSheet(
    language: AppLanguage,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<HalalStatus?>(null) }

    val filteredList = remember(searchQuery, selectedFilter) {
        val q = searchQuery.trim().lowercase()
        InitialData.eAdditivesDirectory.filter { item ->
            val matchesQuery = q.isEmpty() ||
                    item.code.lowercase().contains(q) ||
                    item.name.lowercase().contains(q) ||
                    item.description.lowercase().contains(q) ||
                    item.origin.lowercase().contains(q) ||
                    item.alternateNames.any { it.lowercase().contains(q) }
            val matchesFilter = selectedFilter == null || item.status == selectedFilter
            matchesQuery && matchesFilter
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NaturalWarmBg,
        modifier = modifier.testTag("e_additives_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.getEAdditivesTitle(language),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NaturalTextDark
                    )
                    Text(
                        text = when (language) {
                            AppLanguage.EN -> "Comprehensive EU & US E-numbers & additives directory"
                            AppLanguage.DE -> "E-Nummern und Zusatzstoffdatenbank für Europa & USA"
                            AppLanguage.FR -> "Base de données des additifs pour l'Europe et les USA"
                            AppLanguage.TR -> "Katkı maddesi veri tabanı ve helallik rehberi"
                            AppLanguage.AR -> "دليل شامل لأرقام E والمضافات الغذائية"
                        },
                        fontSize = 12.sp,
                        color = NaturalTextMuted
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NaturalTextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("e_additive_search_input"),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = NaturalTextDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                placeholder = {
                    Text(
                        text = when (language) {
                            AppLanguage.EN -> "Search E471, Gelatin, Carmine, E120, DATEM..."
                            AppLanguage.DE -> "E471, Gelatine, Karmin, E120 suchen..."
                            AppLanguage.FR -> "Rechercher E471, Gélatine, Carmin..."
                            AppLanguage.TR -> "E471, Jelatin, Karmin, E120, Şellak ara..."
                            AppLanguage.AR -> "ابحث عن E471، جيلاتين، كارمين..."
                        },
                        color = NaturalSearchPlaceholder,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary)
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NaturalTextDark,
                    unfocusedTextColor = NaturalTextDark,
                    focusedPlaceholderColor = NaturalSearchPlaceholder,
                    unfocusedPlaceholderColor = NaturalSearchPlaceholder,
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary,
                    unfocusedContainerColor = NaturalWarmSurface,
                    focusedContainerColor = NaturalWarmSurface,
                    unfocusedBorderColor = NaturalWarmBorder,
                    cursorColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("${AppStrings.getAll(language)} (${InitialData.eAdditivesDirectory.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
                    onClick = { selectedFilter = if (selectedFilter == HalalStatus.HARAM) null else HalalStatus.HARAM },
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
                    onClick = { selectedFilter = if (selectedFilter == HalalStatus.SUPHELI) null else HalalStatus.SUPHELI },
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
                    onClick = { selectedFilter = if (selectedFilter == HalalStatus.HELAL) null else HalalStatus.HELAL },
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

            Spacer(modifier = Modifier.height(14.dp))

            // List of E-Additives
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(390.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.code }) { item ->
                    EAdditiveCard(item = item, language = language)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EAdditiveCard(item: EAdditive, language: AppLanguage) {
    val (cardBg, cardBorder) = when (item.status) {
        HalalStatus.HELAL -> Pair(HalalGreenBg, HalalGreenBorder)
        HalalStatus.HARAM -> Pair(HaramRedBg, HaramRedBorder)
        else -> Pair(SuspiciousAmberBg, SuspiciousAmberBorder)
    }

    Surface(
        color = cardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.code,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when (item.status) {
                            HalalStatus.HELAL -> HalalGreenDark
                            HalalStatus.HARAM -> HaramRedDark
                            else -> SuspiciousAmberDark
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = item.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextDark
                    )
                }
                HalalStatusBadge(status = item.status, fontSize = 10.sp, paddingHorizontal = 8.dp, paddingVertical = 2.dp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kaynak: ${item.origin}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimaryDeep
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.description,
                fontSize = 12.sp,
                color = NaturalTextDark.copy(alpha = 0.85f),
                lineHeight = 16.sp
            )

            if (item.alternateNames.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "Alternatif İsimler: ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextMuted
                    )
                    Text(
                        text = item.alternateNames.joinToString(", "),
                        fontSize = 11.sp,
                        color = NaturalTextMuted,
                        lineHeight = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Kullanım Alanı: ${item.commonUsage}",
                fontSize = 11.sp,
                color = NaturalTextMuted
            )
        }
    }
}
