package com.example.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReportEmailTest {

    private val product = FoodProduct(
        barcode = "1234567890123",
        name = "Test Snack",
        brand = "Test Brand",
        category = "Snacks",
        status = HalalStatus.SUPHELI,
        harmfulOrSuspiciousIngredients = listOf("E471 Mono- and Diglycerides"),
        language = AppLanguage.EN
    )

    @Test
    fun `subject includes the product name`() {
        val subject = ReportEmail.buildSubject(product, AppLanguage.EN)
        assertTrue(subject.contains("Test Snack"))
    }

    @Test
    fun `body includes barcode, product, status and flagged ingredients`() {
        val body = ReportEmail.buildBody(product, AppLanguage.EN)
        assertTrue(body.contains("1234567890123"))
        assertTrue(body.contains("Test Snack"))
        assertTrue(body.contains("Test Brand"))
        assertTrue(body.contains("E471 Mono- and Diglycerides"))
    }

    @Test
    fun `body omits the flagged ingredients line when there are none`() {
        val clean = product.copy(status = HalalStatus.HELAL, harmfulOrSuspiciousIngredients = emptyList())
        val body = ReportEmail.buildBody(clean, AppLanguage.EN)
        assertEquals(false, body.contains("Flagged"))
    }

    @Test
    fun `support email is the developer contact address`() {
        assertEquals("fatihgulen@gmail.com", ReportEmail.SUPPORT_EMAIL)
    }
}
