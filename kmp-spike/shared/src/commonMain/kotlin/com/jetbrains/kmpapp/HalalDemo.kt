package com.jetbrains.kmpapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.data.analyzer.HalalAnalyzer
import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab

private val sampleIngredients = listOf(
    "Un, şeker, su, tuz",
    "Su, şeker, jelatin (E441), aroma",
    "Su, şeker, etil alkol, aroma",
    "Süt, peynir, mikrobiyal rennet (E1100)",
)

/**
 * P0/P1 doğrulama ekranı: ortak halal analizörü iPhone'da çalışıyor mu?
 * İçindekiler metnini yazarsın, sonuç anında gelir (Türkçe, mezhep: belirtilmemiş).
 */
@Composable
fun HalalDemo() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            var text by remember { mutableStateOf(sampleIngredients[1]) }
            val product = remember(text) {
                if (text.isBlank()) null
                else HalalAnalyzer.analyzeIngredientsText(
                    productName = "Test",
                    ingredientsText = text,
                    language = AppLanguage.TR,
                    madhhab = Madhhab.UNSPECIFIED,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Halal analizör (ortak kod)", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("İçindekiler") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    sampleIngredients.forEachIndexed { index, sample ->
                        AssistChip(onClick = { text = sample }, label = { Text("Örnek ${index + 1}") })
                    }
                }
                product?.let { result ->
                    Text(
                        text = "Sonuç: ${result.status.label}",
                        style = MaterialTheme.typography.titleMedium,
                        color = colorFor(result.status),
                    )
                    Text(result.reasonOrDetails, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

private fun colorFor(status: HalalStatus): Color = when (status) {
    HalalStatus.HELAL -> Color(0xFF2E7D32)
    HalalStatus.HARAM -> Color(0xFFC62828)
    HalalStatus.SUPHELI -> Color(0xFFEF6C00)
    HalalStatus.BULUNAMADI -> Color(0xFF616161)
}
