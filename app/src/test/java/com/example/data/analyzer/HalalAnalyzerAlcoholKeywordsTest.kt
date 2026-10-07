package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Test

// User-approved 2026-10-07:
// 1. The alcohol rule had no Turkish keywords - a label saying "ALKOL" or "ŞARAP" came out Halal.
//    Turkish suffixes defeat the word-boundary match, so inflected forms are listed explicitly
//    (never prefix matching: "bira" would hit "biraz").
// 2. Non-beverage "alcohols" (polyvinyl/benzyl alcohol, sugar alcohols) and "contains no alcohol"
//    phrasings were flagged Haram because the bare "alcohol"/"alkol" keyword matched them.
class HalalAnalyzerAlcoholKeywordsTest {

    private fun status(text: String) = HalalAnalyzer.analyzeOpenFoodFactsProduct(
        barcode = "0000000000000",
        offProduct = OffProduct(productName = "Test Product", ingredientsText = text),
        language = AppLanguage.TR
    ).status

    @Test
    fun `Turkish alcohol words are haram`() {
        for (text in listOf("su, şeker, alkol", "ALKOL", "kırmızı şarap", "şarabı", "bira", "alkollü", "ETİL ALKOL", "aroma verici (etil alkolü)")) {
            assertEquals("'$text'", HalalStatus.HARAM, status(text))
        }
    }

    @Test
    fun `non-beverage alcohols and negations are not haram`() {
        for (text in listOf(
            "bira mayası", "şeker alkolleri (maltitol)", "sweetener: sugar alcohol", "polyvinyl alcohol",
            "polivinil alkol", "BENZİL ALKOL", "alkol içermez", "alkol içermemektedir", "biraz şeker",
            "şarap sirkesi", "alkolsüz bira"
        )) {
            assertEquals("'$text'", HalalStatus.HELAL, status(text))
        }
    }

    @Test
    fun `a negated item next to a real alcohol still flags the real one`() {
        assertEquals(HalalStatus.HARAM, status("benzyl alcohol, ethyl alcohol"))
        assertEquals(HalalStatus.HARAM, status("polivinil alkol, şarap"))
        assertEquals(HalalStatus.HARAM, status("alkol içermez, bira"))
    }

    // An OCR-dropped comma must not let a negation stem swallow a real alcohol ingredient:
    // "şeker, alkol (%3)" read as "şeker alkol (%3)" has to stay Haram.
    @Test
    fun `dropped comma between sugar and alcohol still flags the alcohol`() {
        assertEquals(HalalStatus.HARAM, status("kakao, şeker alkol (%3)"))
        assertEquals(HalalStatus.HARAM, status("un, bira maya, şeker"))
    }

    @Test
    fun `capitalised negations are not haram`() {
        for (text in listOf("ALKOL İÇERMEZ", "BİRA MAYASI", "ŞARAP SİRKESİ", "ŞEKER ALKOLLERİ", "ŞEKER ALKOLÜ")) {
            assertEquals("'$text'", HalalStatus.HELAL, status(text))
        }
    }

    @Test
    fun `chips agree`() {
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("polyvinyl alcohol"))
        assertEquals(HalalStatus.HARAM, HalalAnalyzer.classifyIngredientToken("ŞARAP"))
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("bira mayası"))
    }
}
