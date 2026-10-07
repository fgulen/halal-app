package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Phase 2: the only sourced overrides (user-approved 2026-10-07) - HANAFI, Abu Hanifa's view:
// rennet from an unslaughtered halal-species animal is permissible (Diyanet DİYK 15.09.2020
// agrees), and whey follows rennet (our inference). Pepsin and other enzymes are NOT covered.
class HalalAnalyzerMadhhabOverrideTest {

    private fun analyze(text: String, madhhab: Madhhab, language: AppLanguage = AppLanguage.EN) =
        HalalAnalyzer.analyzeOpenFoodFactsProduct(
            barcode = "0000000000000",
            offProduct = OffProduct(productName = "Test Product", ingredientsText = text),
            language = language,
            madhhab = madhhab
        )

    @Test
    fun `animal rennet is permissible under Hanafi`() {
        assertEquals(HalalStatus.HELAL, analyze("milk, salt, animal rennet", Madhhab.HANAFI).status)
    }

    @Test
    fun `animal rennet stays doubtful under the other madhhabs and the default`() {
        for (m in listOf(Madhhab.UNSPECIFIED, Madhhab.SHAFII, Madhhab.MALIKI, Madhhab.HANBALI)) {
            assertEquals("under $m", HalalStatus.SUPHELI, analyze("milk, salt, animal rennet", m).status)
        }
    }

    @Test
    fun `pepsin is not covered by the Hanafi rennet override`() {
        assertEquals(HalalStatus.SUPHELI, analyze("milk, salt, pepsin", Madhhab.HANAFI).status)
    }

    @Test
    fun `rennet together with pepsin stays doubtful under Hanafi`() {
        assertEquals(HalalStatus.SUPHELI, analyze("milk, rennet, pepsin", Madhhab.HANAFI).status)
    }

    @Test
    fun `whey follows rennet under Hanafi`() {
        assertEquals(HalalStatus.HELAL, analyze("sugar, whey powder, cocoa", Madhhab.HANAFI).status)
    }

    @Test
    fun `whey with E471 stays doubtful under Hanafi because of E471`() {
        val result = analyze("sugar, whey, emulsifier (E471)", Madhhab.HANAFI)
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertEquals(listOf("E471 Mono- and Diglycerides"), result.harmfulOrSuspiciousIngredients)
    }

    @Test
    fun `pork rennet is still haram under Hanafi`() {
        assertEquals(HalalStatus.HARAM, analyze("milk, pork rennet", Madhhab.HANAFI).status)
    }

    // Each claim carries the link that actually states it: the Hanafi split (Abu Hanifa vs. Abu
    // Yusuf/Muhammad) -> thehalallife; Diyanet's own ruling -> the Diyanet fatwa.
    @Test
    fun `Hanafi rennet note cites the Hanafi source and the Diyanet fatwa separately`() {
        val reason = analyze("milk, salt, animal rennet", Madhhab.HANAFI).reasonOrDetails
        assertTrue(reason.contains("Abu Hanifa"))
        assertTrue(reason.contains("https://thehalallife.co.uk/animal-rennet/"))
        assertTrue(reason.contains("https://kurul.diyanet.gov.tr/tr/fetva/peynir-yapiminda-kullanilan-mayalarin-dini-hukmu-nedir/0193c42d-aa60-7126-92d8-9c538f6b513b"))
    }

    // The other-madhhab claim ("Maliki, Shafi'i and most Hanbali: impure") comes from IslamQA 2841.
    @Test
    fun `default verdict on rennet mentions the Hanafi difference with its sources`() {
        val result = analyze("milk, salt, animal rennet", Madhhab.UNSPECIFIED)
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertTrue(result.reasonOrDetails.contains("https://thehalallife.co.uk/animal-rennet/"))
        assertTrue(result.reasonOrDetails.contains("https://islamqa.info/en/answers/2841"))
    }

    // User decision 2026-10-07: Arabic readers get the Arabic edition of IslamQA 2841 (verified to
    // state both the Abu Hanifa/Ahmad view and the majority view) instead of English/Turkish pages.
    @Test
    fun `Arabic rennet notes cite only the Arabic source`() {
        for (m in listOf(Madhhab.HANAFI, Madhhab.UNSPECIFIED)) {
            val reason = analyze("milk, salt, animal rennet", m, AppLanguage.AR).reasonOrDetails
            assertTrue("under $m: $reason", reason.contains("https://islamqa.info/ar/answers/2841"))
            assertFalse("under $m: $reason", reason.contains("thehalallife") || reason.contains("kurul.diyanet.gov.tr") || reason.contains("islamqa.info/en/"))
        }
    }

    // A rennet/whey difference can't change a Haram verdict, so no madhhab note may claim the
    // (pork) rennet is or would be permissible.
    @Test
    fun `no madhhab note on a haram product`() {
        for (m in listOf(Madhhab.HANAFI, Madhhab.UNSPECIFIED)) {
            for (lang in listOf(AppLanguage.EN, AppLanguage.TR)) {
                val reason = analyze("milk, pork rennet, whey", m, lang).reasonOrDetails
                assertFalse("under $m/$lang: $reason", reason.contains("Abu Hanifa") || reason.contains("Ebû Hanîfe"))
                assertFalse("under $m/$lang: $reason", reason.contains("thehalallife") || reason.contains("çıkarım") || reason.contains("inference"))
            }
        }
    }

    @Test
    fun `whey note says it is an inference and does not cite the rennet fatwa as its source`() {
        val reason = analyze("sugar, whey powder", Madhhab.HANAFI, AppLanguage.TR).reasonOrDetails
        assertTrue(reason.contains("çıkarım"))
        assertFalse(reason.contains("kurul.diyanet.gov.tr"))
    }

    @Test
    fun `no madhhab note when no rennet or whey keyword matched`() {
        val reason = analyze("water, sugar, pepsin", Madhhab.UNSPECIFIED).reasonOrDetails
        assertFalse(reason.contains("Abu Hanifa"))
    }

    @Test
    fun `chip colours follow the same override`() {
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("animal rennet", Madhhab.HANAFI))
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("whey powder", Madhhab.HANAFI))
        assertEquals(HalalStatus.SUPHELI, HalalAnalyzer.classifyIngredientToken("pepsin", Madhhab.HANAFI))
        assertEquals(HalalStatus.SUPHELI, HalalAnalyzer.classifyIngredientToken("animal rennet", Madhhab.SHAFII))
        assertEquals(HalalStatus.HARAM, HalalAnalyzer.classifyIngredientToken("pork rennet", Madhhab.HANAFI))
    }
}
