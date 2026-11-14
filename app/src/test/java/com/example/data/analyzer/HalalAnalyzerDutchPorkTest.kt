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
}
