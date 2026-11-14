package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Test

// Regression: EAN 4061461720452 "Kabanos Classic" was shown as Halal. Its OFF ingredients are
// garbled, but the Dutch pork word "varkensvlees" is in the text and must be Haram (pork).
class HalalAnalyzerDutchPorkTest {

    private fun analyze(text: String) =
        HalalAnalyzer.analyzeOpenFoodFactsProduct(
            barcode = "4061461720452",
            offProduct = OffProduct(productName = "Kabanos Classic", ingredientsText = text),
            language = AppLanguage.EN,
            madhhab = Madhhab.UNSPECIFIED
        )

    @Test
    fun `Dutch varkensvlees in ingredients is haram`() {
        val text = "Klassiek varkensvlees, zetmeel, specerijen, specerijenextracten, aroma's, eiwit, suiker, antioxidant, natriumnitriet"
        assertEquals(HalalStatus.HARAM, analyze(text).status)
    }

    @Test
    fun `footnote mark after varkensvlees is still haram`() {
        assertEquals(HalalStatus.HARAM, analyze("68% varkensvleesª, water, zout").status)
    }

    @Test
    fun `new pork compounds are haram`() {
        assertEquals(HalalStatus.HARAM, analyze("varkensvet, zout, specerijen").status)
        assertEquals(HalalStatus.HARAM, analyze("Coscia di suino, sale, destrosio").status)
        assertEquals(HalalStatus.HARAM, analyze("Jamón de cerdo, sal, agua").status)
    }

    @Test
    fun `declared pork-free is not haram by the pork rule`() {
        assertEquals(HalalStatus.HELAL, analyze("Vegetarische worst, zonder varkensvlees, water, zout").status)
        assertEquals(HalalStatus.HELAL, analyze("Salchicha, sin cerdo, agua, sal").status)
        assertEquals(HalalStatus.HELAL, analyze("Würstchen, ohne Schweinefleisch, Wasser, Salz").status)
    }
}
