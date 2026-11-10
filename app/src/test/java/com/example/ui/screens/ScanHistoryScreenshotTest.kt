package com.example.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
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

// P3d öncesi taban çizgisi: geçmiş kartı (helal/haram) ve boş geçmiş ekranı.
// Taşıma sonrası görüntüler md5 ile aynı olmalı (src/test/screenshots/history-*.png).
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class ScanHistoryScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    private fun product(status: HalalStatus) = FoodProduct(
        barcode = "1234567890123",
        name = "Test Product",
        brand = "Test Brand",
        category = "Test",
        status = status,
        reasonOrDetails = "Test reason",
        scannedAt = 1_700_000_000_000L
    )

    @Test
    fun historyCardHelal() {
        composeTestRule.setContent {
            HalalKontrolTheme {
                HistoryProductCard(product = product(HalalStatus.HELAL), language = AppLanguage.EN, onClick = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/history-card-helal-en.png")
    }

    @Test
    fun historyCardHaram() {
        composeTestRule.setContent {
            HalalKontrolTheme {
                HistoryProductCard(product = product(HalalStatus.HARAM), language = AppLanguage.TR, onClick = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/history-card-haram-tr.png")
    }

    @Test
    fun emptyHistoryScreen() {
        composeTestRule.setContent {
            HalalKontrolTheme {
                ScanHistoryScreen(
                    scanHistory = emptyList(),
                    selectedFilter = null,
                    language = AppLanguage.DE,
                    onSelectFilter = {},
                    onProductClick = {},
                    onClearHistory = {},
                    onScanNewClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/history-empty-de.png")
    }
}
