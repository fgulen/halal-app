package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.ui.theme.HalalKontrolTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

// P3d öncesi taban çizgisi: sonuç kartının bölümleri (durum başlığı, helal/haram/şüpheli bölümleri).
// Taşıma sonrası görüntüler md5 ile aynı olmalı (src/test/screenshots/product-*.png).
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class ProductResultScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    private fun product(status: HalalStatus, flagged: List<String> = emptyList()) = FoodProduct(
        barcode = "1234567890123",
        name = "Test Product",
        brand = "Test Brand",
        category = "Test",
        status = status,
        reasonOrDetails = "Test reason",
        harmfulOrSuspiciousIngredients = flagged,
        allIngredients = listOf("sugar", "salt", "gelatin"),
        nutriScore = "B",
        novaGroup = 3
    )

    private fun capture(name: String, language: AppLanguage, content: @androidx.compose.runtime.Composable () -> Unit) {
        composeTestRule.setContent {
            HalalKontrolTheme {
                Column(modifier = Modifier.padding(16.dp)) { content() }
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/product-$name.png")
    }

    @Test
    fun statusHeaderHelal() = capture("header-helal-en", AppLanguage.EN) {
        StatusHeaderCard(product = product(HalalStatus.HELAL), language = AppLanguage.EN)
    }

    @Test
    fun statusHeaderHaram() = capture("header-haram-tr", AppLanguage.TR) {
        StatusHeaderCard(product = product(HalalStatus.HARAM), language = AppLanguage.TR)
    }

    @Test
    fun helalSuccessSection() = capture("helal-de", AppLanguage.DE) {
        HelalSuccessSection(product = product(HalalStatus.HELAL), language = AppLanguage.DE)
    }

    @Test
    fun suspiciousWarningSection() = capture("suspicious-ar", AppLanguage.AR) {
        SuspiciousWarningSection(
            product = product(HalalStatus.SUPHELI, listOf("E471 Unclear source")),
            language = AppLanguage.AR
        )
    }

    @Test
    fun haramWarningSection() = capture("haram-fr", AppLanguage.FR) {
        HaramWarningSection(
            product = product(HalalStatus.HARAM, listOf("Pork gelatin")),
            language = AppLanguage.FR
        )
    }

    @Test
    fun disclaimerLine() = capture("disclaimer-en", AppLanguage.EN) {
        ShortDisclaimerLine(language = AppLanguage.EN)
    }
}
