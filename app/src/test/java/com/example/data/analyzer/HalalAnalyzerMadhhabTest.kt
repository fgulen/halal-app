package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.Madhhab
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Test

// Invariance: a madhhab may only change a verdict where a sourced override exists. Today that is
// exactly HANAFI on rennet and whey (Phase 2, see HalalAnalyzerMadhhabOverrideTest) - those two
// (madhhab, sample) pairs are excluded below; everything else must equal the UNSPECIFIED result.
class HalalAnalyzerMadhhabTest {

    private val samples = listOf(
        "water, pork gelatin, sugar",
        "milk, salt, animal rennet",
        "sugar, whey powder, cocoa",
        "sugar, colour: carmine (E120)",
        "flour, emulsifier (E471), salt",
        "water, sugar, natural flavouring"
    )

    private val overriddenUnderHanafi = setOf("milk, salt, animal rennet", "sugar, whey powder, cocoa", "animal rennet", "whey powder")

    private fun isExpectedOverride(sample: String, madhhab: Madhhab) =
        madhhab == Madhhab.HANAFI && sample in overriddenUnderHanafi

    private fun analyze(text: String, madhhab: Madhhab) = HalalAnalyzer.analyzeOpenFoodFactsProduct(
        barcode = "0000000000000",
        offProduct = OffProduct(productName = "Test Product", ingredientsText = text),
        language = AppLanguage.EN,
        madhhab = madhhab
    )

    @Test
    fun `every madhhab gives the same verdict and flags as UNSPECIFIED`() {
        for (text in samples) {
            val baseline = analyze(text, Madhhab.UNSPECIFIED)
            for (madhhab in Madhhab.values()) {
                if (isExpectedOverride(text, madhhab)) continue
                val result = analyze(text, madhhab)
                assertEquals("status for '$text' under $madhhab", baseline.status, result.status)
                assertEquals("flags for '$text' under $madhhab", baseline.harmfulOrSuspiciousIngredients, result.harmfulOrSuspiciousIngredients)
            }
        }
    }

    @Test
    fun `result is stamped with the madhhab it was analyzed under`() {
        for (madhhab in Madhhab.values()) {
            assertEquals(madhhab, analyze("milk, salt, animal rennet", madhhab).madhhab)
        }
    }

    @Test
    fun `ingredient-text path is stamped with its madhhab`() {
        val result = HalalAnalyzer.analyzeIngredientsText(
            productName = "Photo",
            ingredientsText = "milk, salt, animal rennet",
            language = AppLanguage.EN,
            madhhab = Madhhab.HANAFI
        )
        assertEquals(Madhhab.HANAFI, result.madhhab)
    }

    @Test
    fun `chip classification is the same under every madhhab`() {
        for (token in listOf("animal rennet", "whey powder", "pork gelatin", "E471", "sugar")) {
            val baseline = HalalAnalyzer.classifyIngredientToken(token, Madhhab.UNSPECIFIED)
            for (madhhab in Madhhab.values()) {
                if (isExpectedOverride(token, madhhab)) continue
                assertEquals("'$token' under $madhhab", baseline, HalalAnalyzer.classifyIngredientToken(token, madhhab))
            }
        }
    }
}
