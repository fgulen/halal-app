package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Turkish packaging is very often printed in capitals. lowercase(Locale.ROOT) maps "İ" to
// "i" + U+0307 and "I" to "i" (never "ı"), so before the shared normalization no capitalised
// Turkish keyword ever matched. These pin every matching path to the same normalization.
class HalalAnalyzerTurkishCaseTest {

    private fun analyze(text: String, madhhab: Madhhab = Madhhab.UNSPECIFIED) =
        HalalAnalyzer.analyzeOpenFoodFactsProduct(
            barcode = "0000000000000",
            offProduct = OffProduct(productName = "Test Product", ingredientsText = text),
            language = AppLanguage.TR,
            madhhab = madhhab
        )

    @Test
    fun `caps beef gelatin fires the source-stated rule, not the generic one`() {
        val result = analyze("ŞEKER, GLUKOZ ŞURUBU, SIĞIR JELATİNİ")
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertEquals(listOf("Sığır/Balık Jelatini (Kaynak Belirtilmiş)"), result.harmfulOrSuspiciousIngredients)
    }

    @Test
    fun `caps carmine without an E-code is haram`() {
        assertEquals(HalalStatus.HARAM, analyze("ŞEKER, RENKLENDİRİCİ: KARMİN").status)
    }

    @Test
    fun `caps microbial rennet is not flagged`() {
        assertEquals(HalalStatus.HELAL, analyze("SÜT, TUZ, MİKROBİYAL PEYNİR MAYASI").status)
    }

    @Test
    fun `caps rennet chip follows the madhhab`() {
        assertEquals(HalalStatus.SUPHELI, HalalAnalyzer.classifyIngredientToken("PEYNİR MAYASI"))
        assertEquals(HalalStatus.HELAL, HalalAnalyzer.classifyIngredientToken("PEYNİR MAYASI", Madhhab.HANAFI))
    }

    @Test
    fun `caps rennet is permissible under Hanafi`() {
        assertEquals(HalalStatus.HELAL, analyze("PASTÖRİZE İNEK SÜTÜ, TUZ, PEYNİR MAYASI", Madhhab.HANAFI).status)
    }

    @Test
    fun `caps Turkish nutrition table is detected`() {
        assertTrue(HalalAnalyzer.looksLikeNutritionTableText("BESİN DEĞERLERİ ENERJİ 250 KKAL KARBONHİDRAT 30 G"))
    }

    @Test
    fun `caps Turkish ingredients header is recognised as an ingredients section`() {
        assertFalse(HalalAnalyzer.looksLikeNutritionTableText("İÇİNDEKİLER: ŞEKER, KAKAO. BESİN DEĞERLERİ ENERJİ 250 KKAL"))
    }

    @Test
    fun `all-caps English label still works`() {
        assertEquals(HalalStatus.HARAM, analyze("INGREDIENTS: SUGAR, PORK GELATIN, WINE").status)
    }
}
