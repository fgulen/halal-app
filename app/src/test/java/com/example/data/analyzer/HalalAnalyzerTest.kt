package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Pure-function tests: no Android framework dependency, so no Robolectric runner needed.
// Each test pins down a specific bug found and fixed during the 2026-08-28 reliability pass.
class HalalAnalyzerTest {

    private fun analyze(
        ingredientsText: String,
        additivesTags: List<String>? = null,
        labelsTags: List<String>? = null,
        ingredientsAnalysisTags: List<String>? = null,
        language: AppLanguage = AppLanguage.EN
    ) = HalalAnalyzer.analyzeOpenFoodFactsProduct(
        barcode = "0000000000000",
        offProduct = OffProduct(
            productName = "Test Product",
            ingredientsText = ingredientsText,
            additivesTags = additivesTags,
            labelsTags = labelsTags,
            ingredientsAnalysisTags = ingredientsAnalysisTags
        ),
        language = language
    )

    @Test
    fun `pork gelatin is Haram`() {
        val result = analyze("water, pork gelatin, sugar")
        assertEquals(HalalStatus.HARAM, result.status)
    }

    @Test
    fun `alcohol-free flavoring is not flagged Haram`() {
        // Regression: containsKeyword treats '-' as a word boundary, so "alcohol-free" used to
        // share a boundary with the bare "alcohol" keyword and trip the Haram rule.
        val result = analyze("alcohol-free natural flavoring, water, sugar")
        assertFalse(
            "alcohol-free product must not be flagged as containing alcohol",
            result.flaggedDetails.any { it.eCode == null && it.name.contains("Alcohol", ignoreCase = true) }
        )
        assertEquals(HalalStatus.SUPHELI, result.status) // no claim -> doubt by default, not Haram
    }

    @Test
    fun `E1200 additive tag is not confused with E120 carmine`() {
        // Regression: additive-tag matching used tag.contains(keyword), so tag "e1200"
        // (polydextrose) matched keyword "e120" (carmine) as a plain substring.
        val result = analyze(
            ingredientsText = "polydextrose, water",
            additivesTags = listOf("en:e1200")
        )
        assertFalse(
            "E1200 must not be flagged as E120 carmine",
            result.flaggedDetails.any { it.eCode == "E120" }
        )
        assertFalse(result.status == HalalStatus.HARAM)
    }

    @Test
    fun `plain beef with no halal claim is Suspicious, not Halal`() {
        // Regression: meat/poultry used to pass straight through to a green Helal verdict.
        val result = analyze("beef, water, salt")
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertTrue(
            "reason should explain the meat/slaughter concern, not talk about additive origin",
            result.reasonOrDetails.contains("zabiha", ignoreCase = true)
        )
        assertFalse(result.reasonOrDetails.contains("additives of unverified origin", ignoreCase = true))
    }

    @Test
    fun `beef with an explicit halal label reaches Halal`() {
        // The meat gate must suppress itself when the product already carries a halal claim.
        val result = analyze(
            ingredientsText = "chicken, water, salt",
            labelsTags = listOf("en:halal")
        )
        assertEquals(HalalStatus.HELAL, result.status)
    }

    @Test
    fun `free-text halal mentions do not grant Halal or suppress the meat gate`() {
        // Regression: hasHalalClaim used to do a plain .contains("halal") on free ingredient
        // text, which also matched the negated phrase "not halal certified".
        val result = analyze("beef, not halal certified, water")
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertTrue(
            "meat rule must still fire despite 'halal' appearing in the negated free text",
            result.flaggedDetails.any { it.name.contains("Meat", ignoreCase = true) }
        )
    }

    @Test
    fun `OFF non-vegan analysis tag does not grant a false Halal verdict`() {
        // Regression: OFF's ingredients_analysis_tags value "en:non-vegan" contains the
        // substring "vegan", so a .contains check treated it as a positive vegan claim.
        val result = analyze(
            ingredientsText = "sugar, water, salt",
            ingredientsAnalysisTags = listOf("en:non-vegan")
        )
        assertFalse(result.status == HalalStatus.HELAL)
        assertEquals(HalalStatus.SUPHELI, result.status)
    }

    @Test
    fun `plain product with a real halal label and no flags reaches Halal`() {
        val result = analyze(
            ingredientsText = "water, sugar, salt",
            labelsTags = listOf("en:halal")
        )
        assertEquals(HalalStatus.HELAL, result.status)
        assertTrue(result.halalCertificate?.contains("Halal") == true)
    }

    @Test
    fun `an OFF vegan analysis tag alone is enough for Halal when nothing is flagged`() {
        val result = analyze(
            ingredientsText = "water, sugar, salt",
            ingredientsAnalysisTags = listOf("en:vegan")
        )
        assertEquals(HalalStatus.HELAL, result.status)
    }
}
