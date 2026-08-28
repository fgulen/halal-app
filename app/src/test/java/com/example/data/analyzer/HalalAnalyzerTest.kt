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
        // Nothing prohibited or doubtful was found, so this is a Halal screening result even
        // without an explicit halal/vegan claim/label (see Rule 4 in HalalAnalyzer).
        assertEquals(HalalStatus.HELAL, result.status)
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
    fun `OFF non-vegan analysis tag does not grant a false vegan certificate`() {
        // Regression: OFF's ingredients_analysis_tags value "en:non-vegan" contains the
        // substring "vegan", so a .contains check treated it as a positive vegan claim.
        // Status is HELAL on this ingredient list regardless (nothing was flagged - Rule 4),
        // but the bug this test guards against is the certificate: "en:non-vegan" must never be
        // read as a vegan/plant-based claim and must not produce a "Vegan Label" certificate.
        val result = analyze(
            ingredientsText = "sugar, water, salt",
            ingredientsAnalysisTags = listOf("en:non-vegan")
        )
        assertEquals(HalalStatus.HELAL, result.status)
        assertEquals(null, result.halalCertificate)
    }

    @Test
    fun `vegetarian-only dairy product does not get a false Plant-Based-Vegan certificate`() {
        // Regression: hasVeganClaim used to treat "en:vegetarian" as equivalent to a vegan/
        // plant-based claim. Vegetarian permits dairy and animal rennet, so a vegetarian-tagged
        // product like butter (OFF-tagged en:non-vegan) must not be shown a "100% Plant-Based /
        // Vegan Label" certificate it has no basis for.
        val result = analyze(
            ingredientsText = "cream, salt",
            ingredientsAnalysisTags = listOf("en:vegetarian", "en:non-vegan")
        )
        assertEquals(HalalStatus.HELAL, result.status) // nothing prohibited/doubtful found
        assertEquals(null, result.halalCertificate)
        assertFalse(
            "vegetarian must not be presented as a plant-based/vegan claim",
            (result.halalCertificate ?: "").contains("Vegan", ignoreCase = true)
        )
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

    @Test
    fun `beef gelatin is Suspicious with a source-aware reason, not the generic unspecified one`() {
        val result = analyze("water, beef gelatin, sugar")
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertTrue(
            "should flag the source-stated beef/fish gelatin rule, not the generic unspecified one",
            result.flaggedDetails.any { it.name.contains("Beef", ignoreCase = true) || it.name.contains("Fish", ignoreCase = true) }
        )
        assertFalse(
            "must not also show the generic 'unspecified source' gelatin flag once source is known",
            result.flaggedDetails.any { it.name.contains("Unspecified", ignoreCase = true) }
        )
        assertFalse(
            "reason text must not claim the source is unspecified when the text says 'beef'",
            result.reasonOrDetails.contains("does not specify", ignoreCase = true)
        )
    }

    @Test
    fun `fish gelatin is Suspicious with a source-aware reason`() {
        val result = analyze("water, fish gelatin, sugar")
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertTrue(
            result.flaggedDetails.any { it.name.contains("Beef", ignoreCase = true) || it.name.contains("Fish", ignoreCase = true) }
        )
    }

    @Test
    fun `plain unspecified gelatin still falls back to the generic suspicious rule`() {
        // Regression guard: the new source-specific rule must not swallow the plain case.
        val result = analyze("water, gelatin, sugar")
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertTrue(result.flaggedDetails.any { it.name.contains("Unspecified", ignoreCase = true) })
    }

    @Test
    fun `vegetable gelatin claim is not flagged Suspicious`() {
        // Regression: the bare "gelatin" keyword matched inside "vegetable gelatin" and its
        // DE/FR/TR/ES equivalents, flagging an explicit plant-based claim as if the source
        // were unspecified.
        val result = analyze("water, vegetable gelatin, sugar")
        assertFalse(
            "an explicit vegetable/plant gelatin claim must not be flagged",
            result.flaggedDetails.any { it.name.contains("Gelatin", ignoreCase = true) }
        )
        assertEquals(HalalStatus.HELAL, result.status)
    }

    @Test
    fun `beef gelatin with e441 additive tag does not also show a contradicting Suspicious flag`() {
        // Regression: E441 is modeled as pork-only in HARAM_RULES, so a product whose additive
        // tags include e441 goes HARAM even when the ingredient text says "beef" - but the
        // beef/fish Suspicious rule must not ALSO fire and render a "not pork" card under the
        // same Haram "Prohibited Ingredients" section.
        val result = analyze(
            ingredientsText = "water, beef gelatin (E441), sugar",
            additivesTags = listOf("en:e441")
        )
        assertEquals(HalalStatus.HARAM, result.status)
        assertFalse(
            "must not also carry a Suspicious beef/fish gelatin flag alongside the Haram pork flag",
            result.flaggedDetails.any { it.name.contains("Beef", ignoreCase = true) || it.name.contains("Fish", ignoreCase = true) }
        )
    }

    @Test
    fun `classifyIngredientToken does not flag an explicit vegetable gelatin claim`() {
        // Regression: the ingredient-chip classifier (separate code path from the main verdict
        // analysis) didn't strip negation phrases, so a chip could show amber/Suspicious even
        // when the overall verdict was HELAL for the same reason.
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("vegetable gelatin"))
    }

    @Test
    fun `plain Turkish jelatin with no stated source is flagged Suspicious`() {
        val result = analyze("su, jelatin, seker", language = AppLanguage.TR)
        assertEquals(HalalStatus.SUPHELI, result.status)
    }

    @Test
    fun `Turkish sigir jelatini is not confused with the bare jelatin keyword`() {
        // Regression guard: adding bare "jelatin" must not double-match inside "jelatini".
        val result = analyze("su, sığır jelatini, şeker", language = AppLanguage.TR)
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertFalse(
            "should flag the specific beef/fish rule, not also the generic unspecified one",
            result.flaggedDetails.any { it.name.contains("Unspecified", ignoreCase = true) || it.name.contains("Belirtilmemiş", ignoreCase = true) }
        )
    }
}
