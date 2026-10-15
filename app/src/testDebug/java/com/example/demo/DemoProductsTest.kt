package com.example.demo

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import org.junit.Assert.assertEquals
import org.junit.Test

// Pins the verdicts the store video/screenshots rely on, in every language the demo ships in.
// If an analyzer change flips one of these, the marketing material would silently show a
// different story - this test makes that a conscious decision instead.
class DemoProductsTest {

    private val expected = mapOf(
        "2001000000017" to HalalStatus.HELAL,   // crackers
        "2001000000024" to HalalStatus.HARAM,   // pork gelatine
        "2001000000031" to HalalStatus.SUPHELI, // whey, no madhhab chosen
        "2001000000048" to HalalStatus.HELAL,   // cherry soda
        "2001000000055" to HalalStatus.HELAL,   // biscuits
        "2001000000062" to HalalStatus.HARAM    // rum
    )

    private val languages = listOf(AppLanguage.EN, AppLanguage.DE, AppLanguage.FR, AppLanguage.AR)

    @Test
    fun `demo verdicts are identical in every demo language`() {
        val report = StringBuilder()
        for (language in languages) {
            for (item in DemoProducts.items) {
                val product = item.analyze(language, Madhhab.UNSPECIFIED)
                report.appendLine("$language ${item.barcode} ${product.name} -> ${product.status} ${product.harmfulOrSuspiciousIngredients}")
            }
        }
        println(report)
        for (language in languages) {
            for (item in DemoProducts.items) {
                assertEquals(
                    "$language ${item.barcode}",
                    expected.getValue(item.barcode),
                    item.analyze(language, Madhhab.UNSPECIFIED).status
                )
            }
        }
    }

    @Test
    fun `hanafi turns the whey chocolate halal in every demo language`() {
        val chocolate = DemoProducts.items.first { it.barcode == "2001000000031" }
        for (language in languages) {
            assertEquals("$language", HalalStatus.HELAL, chocolate.analyze(language, Madhhab.HANAFI).status)
        }
    }
}
