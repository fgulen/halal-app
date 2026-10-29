package com.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.LayoutDirection
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

// Renders the health gauge for the cases that are easy to get wrong: a middle grade, the best and
// worst grades, an Arabic (RTL) locale, and NOVA without a Nutri-Score. Screenshots are written to
// src/test/screenshots/ so they can be read and checked by eye.
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class HealthGaugeScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    private fun product(nutri: String?, nova: Int?) = FoodProduct(
        barcode = "0",
        name = "Test",
        brand = "Test",
        category = "Test",
        status = HalalStatus.HELAL,
        nutriScore = nutri,
        novaGroup = nova
    )

    private fun capture(name: String, product: FoodProduct, language: AppLanguage) {
        composeTestRule.setContent {
            // Arabic runs right-to-left in the app, so the RTL case is rendered that way too.
            val direction = if (language == AppLanguage.AR) LayoutDirection.Rtl else LayoutDirection.Ltr
            HalalKontrolTheme {
                CompositionLocalProvider(LocalLayoutDirection provides direction) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        HealthGaugeCard(product = product, language = language)
                    }
                }
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/health-gauge-$name.png")
    }

    @Test
    fun gauge_grade_c_turkish() = capture("c-tr", product("C", 4), AppLanguage.TR)

    @Test
    fun gauge_grade_a_arabic_rtl() = capture("a-ar", product("A", 1), AppLanguage.AR)

    @Test
    fun gauge_grade_e_english() = capture("e-en", product("E", 4), AppLanguage.EN)

    @Test
    fun gauge_nova_only_german() = capture("nova-only-de", product(null, 3), AppLanguage.DE)
}
