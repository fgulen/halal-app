package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Test

// Flag cards (name, reason, origin) must be in the UI language. Before 2026-10-22 the rules only
// had EN/TR text, so DE/FR/AR result screens showed English ("E441 Pork Gelatin").
class HalalAnalyzerLocalizationTest {

    private fun analyze(text: String, language: AppLanguage, additives: List<String>? = null) =
        HalalAnalyzer.analyzeOpenFoodFactsProduct(
            barcode = "0000000000000",
            offProduct = OffProduct(productName = "Test", ingredientsText = text, additivesTags = additives),
            language = language
        )

    // One keyword per rule, so every rule's card is produced at least once.
    private val haramText = "pork gelatin, bacon, carmine, alcohol, bone phosphate, blood plasma"
    private val suspiciousText = "e471, datem, glycerol, shellac, l-cysteine, disodium inosinate, " +
        "fish gelatin, bovine collagen, rennet, whey, chicken"

    // Names that are legitimately spelled the same as in English.
    private val sameAsEnglish = setOf(AppLanguage.DE to "E422 Glycerin / Glycerol")

    @Test
    fun `every flag card and suggestion is translated in DE, FR, TR and AR`() {
        for (text in listOf(haramText, suspiciousText)) {
            val enResult = analyze(text, AppLanguage.EN)
            val en = enResult.flaggedDetails
            for (lang in listOf(AppLanguage.DE, AppLanguage.FR, AppLanguage.TR, AppLanguage.AR)) {
                val result = analyze(text, lang)
                val local = result.flaggedDetails
                assertEquals("same rules must fire in $lang", en.size, local.size)
                // Rules fire in a fixed order and dedup is by rule id, so cards pair up by position.
                en.zip(local).forEach { (e, l) ->
                    if ((lang to l.name) !in sameAsEnglish) {
                        assertNotEquals("name of '${e.name}' is untranslated in $lang", e.name, l.name)
                    }
                    assertNotEquals("reason of '${e.name}' is untranslated in $lang", e.reason, l.reason)
                    assertNotEquals("origin of '${e.name}' is untranslated in $lang", e.origin, l.origin)
                }
                enResult.alternatives.zip(result.alternatives).forEach { (e, l) ->
                    assertNotEquals("suggestion '$e' is untranslated in $lang", e, l)
                }
            }
        }
    }

    @Test
    fun `German pork gelatin card has no English text`() {
        // "Schweinegelatine" also trips the pork rule ("~schwein"); check the E441 card.
        val card = analyze("Wasser, Schweinegelatine", AppLanguage.DE).flaggedDetails.single { it.eCode == "E441" }
        assertEquals("E441 Schweinegelatine", card.name)
        assertFalse(card.reason.contains("Pork") || card.reason.contains("Prohibited"))
        assertEquals("Schwein", card.origin)
    }

    @Test
    fun `French accented gelatine de porc gets the E441 card`() {
        val cards = analyze("sucre, gélatine de porc", AppLanguage.FR).flaggedDetails
        assertEquals("E441 Gélatine de porc", cards.single { it.eCode == "E441" }.name)
    }

    @Test
    fun `Turkish beef gelatin with e441 tag gets no contradicting doubtful card`() {
        // Regression: the "a pork rule already fired" check matched the English words "Gelatin"/
        // "Pork" in the localized card name, which "E441 Domuz Jelatini" doesn't contain - so in
        // Turkish a doubtful "beef/fish gelatin" card showed under the Haram header.
        val result = analyze("su, sığır jelatini (E441), şeker", AppLanguage.TR, additives = listOf("en:e441"))
        assertEquals(HalalStatus.HARAM, result.status)
        assertFalse(
            "no doubtful gelatin card next to the Haram pork gelatin card",
            result.flaggedDetails.any { it.status == HalalStatus.SUPHELI }
        )
    }
}
