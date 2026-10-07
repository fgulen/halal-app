package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Test

// Two gaps found during the madhhab work (2026-10-07):
// 1. The rennet rule had no Turkish keywords, so a Turkish-labelled cheese's rennet wasn't caught.
// 2. Microbial / vegetable rennet - explicitly permissible per Diyanet - was flagged Doubtful
//    because the bare "rennet" keyword matched inside "microbial rennet".
class HalalAnalyzerRennetKeywordsTest {

    private fun analyze(text: String, madhhab: Madhhab = Madhhab.UNSPECIFIED) =
        HalalAnalyzer.analyzeOpenFoodFactsProduct(
            barcode = "0000000000000",
            offProduct = OffProduct(productName = "Test Product", ingredientsText = text),
            language = AppLanguage.TR,
            madhhab = madhhab
        )

    @Test
    fun `Turkish peynir mayasi is flagged doubtful by default`() {
        assertEquals(HalalStatus.SUPHELI, analyze("pastörize inek sütü, tuz, peynir mayası").status)
    }

    @Test
    fun `Turkish sirden and hayvansal maya are flagged doubtful by default`() {
        assertEquals(HalalStatus.SUPHELI, analyze("süt, tuz, şirden").status)
        assertEquals(HalalStatus.SUPHELI, analyze("süt, tuz, hayvansal maya").status)
    }

    @Test
    fun `Turkish peynir mayasi is permissible under Hanafi`() {
        assertEquals(HalalStatus.HELAL, analyze("pastörize inek sütü, tuz, peynir mayası", Madhhab.HANAFI).status)
    }

    @Test
    fun `bread yeast maya is not mistaken for rennet`() {
        assertEquals(HalalStatus.HELAL, analyze("buğday unu, su, maya, tuz").status)
    }

    @Test
    fun `microbial and vegetable rennet are not flagged`() {
        assertEquals(HalalStatus.HELAL, analyze("milk, salt, microbial rennet").status)
        assertEquals(HalalStatus.HELAL, analyze("milk, salt, vegetable rennet").status)
        assertEquals(HalalStatus.HELAL, analyze("süt, tuz, mikrobiyal peynir mayası").status)
        assertEquals(HalalStatus.HELAL, analyze("süt, tuz, mikrobiyal maya").status)
        assertEquals(HalalStatus.HELAL, analyze("lait, sel, présure microbienne").status)
    }

    @Test
    fun `microbial rennet next to animal rennet still flags the animal rennet`() {
        assertEquals(HalalStatus.SUPHELI, analyze("milk, microbial rennet, animal rennet").status)
    }

    @Test
    fun `microbial rennet written in brackets is not flagged`() {
        assertEquals(HalalStatus.HELAL, analyze("süt, tuz, peynir mayası (mikrobiyal)").status)
        assertEquals(HalalStatus.HELAL, analyze("milk, salt, rennet (microbial)").status)
        assertEquals(HalalStatus.HELAL, analyze("milk, salt, rennet (vegetable)").status)
        assertEquals(HalalStatus.HELAL, analyze("süt, tuz, peynir mayası (bitkisel)").status)
    }

    // Regression for the all-caps Turkish gap (found 2026-10-07): lowercase(Locale.ROOT) turned
    // "İ" into "i" + U+0307 and "I" into "i" (not "ı"), so caps Turkish labels matched nothing.
    @Test
    fun `all-caps Turkish rennet is caught`() {
        assertEquals(HalalStatus.SUPHELI, analyze("PASTÖRİZE İNEK SÜTÜ, TUZ, PEYNİR MAYASI").status)
    }

    @Test
    fun `all-caps Turkish gelatin is caught`() {
        assertEquals(HalalStatus.SUPHELI, analyze("ŞEKER, GLUKOZ ŞURUBU, JELATİN").status)
    }

    @Test
    fun `chip colours agree`() {
        assertEquals(HalalStatus.SUPHELI, HalalAnalyzer.classifyIngredientToken("peynir mayası"))
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("peynir mayası", Madhhab.HANAFI))
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("mikrobiyal peynir mayası"))
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("microbial rennet"))
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("maya"))
    }
}
