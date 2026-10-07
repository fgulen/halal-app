package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Test

// Manufacturer disclaimers ("ürünlerimizde domuz yağı ve katkıları yoktur") are very common on
// Turkish packaging and used to make the product falsely Haram via the "domuz"/"alkol" keywords.
// User chose option (a) on 2026-10-07: strip exact claim phrases only - no sentence-level
// negation, which could hide a real pork ingredient when OFF text has no sentence breaks.
// The phrasings below are taken from real Turkish OFF products.
class HalalAnalyzerDisclaimerTest {

    private fun status(text: String) = HalalAnalyzer.analyzeOpenFoodFactsProduct(
        barcode = "0000000000000",
        offProduct = OffProduct(productName = "Test Product", ingredientsText = text),
        language = AppLanguage.TR
    ).status

    @Test
    fun `real Turkish pork and alcohol disclaimers do not make a product haram`() {
        for (text in listOf(
            "şeker, kakao. ürünlerimizde domuz yağı ve katkıları yoktur",
            "şeker, kakao. ÜRÜNLERİMİZDE DOMUZ YAĞI VE KATKILARI YOKTUR",
            "şeker, kakao. Mamullerimizin Hiçbir Çeşidinde Alkol, Domuz Yağı ve Katkıları Yoktur.",
            "domuz yağı içermez sütlü çikolata",
            "şeker, un. *ürünümüz domuz yağı ve katkılarını içermez",
            "şeker, un. ürünlerimiz domuzdan elde edilen katkı maddesi içermez",
            "sugar, cocoa. contains no alcohol",
            "sugar, cocoa. pork-free"
        )) {
            assertEquals("'$text'", HalalStatus.HELAL, status(text))
        }
    }

    @Test
    fun `a disclaimer never hides a real haram ingredient next to it`() {
        assertEquals(HalalStatus.HARAM, status("domuz yağı içermez, jelatin (domuz)"))
        assertEquals(HalalStatus.HARAM, status("un, domuz yağı, şeker. ürünlerimizde domuz yağı ve katkıları yoktur"))
        assertEquals(HalalStatus.HARAM, status("contains no alcohol, wine"))
        assertEquals(HalalStatus.HARAM, status("şeker, şarap. ürünlerimizde domuz yağı ve katkıları yoktur"))
    }
}
