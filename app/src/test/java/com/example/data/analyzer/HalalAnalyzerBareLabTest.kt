package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.data.remote.OffProduct
import org.junit.Assert.assertEquals
import org.junit.Test

// Unqualified "Lab" (German rennet, origin not stated) must be Doubtful, not Halal.
// Real Open Food Facts strings from German Parmigiano / Grana / pesto labels (2026-11-02).
class HalalAnalyzerBareLabTest {

    private fun analyzeDe(de: String, madhhab: Madhhab = Madhhab.UNSPECIFIED) =
        HalalAnalyzer.analyzeOpenFoodFactsProduct(
            barcode = "0000000000000",
            offProduct = OffProduct(productName = "Test", ingredientsTextDe = de),
            language = AppLanguage.DE,
            madhhab = madhhab
        )

    private fun analyzeGeneric(text: String) =
        HalalAnalyzer.analyzeOpenFoodFactsProduct(
            barcode = "0000000000000",
            offProduct = OffProduct(productName = "Test", ingredientsText = text),
            language = AppLanguage.EN,
            madhhab = Madhhab.UNSPECIFIED
        )

    @Test
    fun `bare German Lab in Parmigiano list is doubtful`() {
        assertEquals(HalalStatus.SUPHELI, analyzeDe("Lab, Salz, Rohmilch").status)
        assertEquals(HalalStatus.SUPHELI, analyzeDe("Milch, Salz, Lab,").status)
        assertEquals(HalalStatus.SUPHELI, analyzeDe("Milch, Salz, Lab. Mit Rohmilch hergestellt.").status)
    }

    @Test
    fun `bare Lab next to lysozyme in Grana list is doubtful`() {
        assertEquals(HalalStatus.SUPHELI, analyzeDe("Milch, Salz, Lab, Konservierungsstoff: Lysozym (aus Ei).").status)
    }

    @Test
    fun `bare Lab stays doubtful under Hanafi because no source is stated`() {
        assertEquals(HalalStatus.SUPHELI, analyzeDe("Milch, Salz, Lab,", Madhhab.HANAFI).status)
    }

    @Test
    fun `tierisches Lab is still flagged once, by the animal rule`() {
        assertEquals(HalalStatus.SUPHELI, analyzeDe("Rohmilch, Speisesalz, tierisches Lab").status)
        assertEquals(HalalStatus.SUPHELI, analyzeDe("Milch, Salz, tierisches Lab").status)
    }

    @Test
    fun `mikrobielles Lab is not flagged`() {
        assertEquals(HalalStatus.HELAL, analyzeDe("Milch, Salz, mikrobielles Lab").status)
        assertEquals(HalalStatus.HELAL, analyzeDe("Milch, Salz, Lab (mikrobiell)").status)
    }

    @Test
    fun `english lab-grown and label words are not flagged`() {
        assertEquals(HalalStatus.HELAL, analyzeGeneric("sugar, water, lab-grown flavour").status)
        assertEquals(HalalStatus.HELAL, analyzeGeneric("sugar, water, label colour").status)
    }

    @Test
    fun `pesto with bare Lab next to Parmigiano is doubtful, pesto with tierisches Lab is doubtful`() {
        // Real-shaped pesto strings (OFF, 2026-11-02). Whole list must not trip a different rule.
        assertEquals(
            HalalStatus.SUPHELI,
            analyzeDe("Sonnenblumenöl, Basilikum 30 %, Cashewkerne, Parmigiano Reggiano Käse 5 % (Milch, Speisesalz, Lab), Maisfasern, Knoblauch, Säureregulator: Milchsäure").status
        )
        assertEquals(
            HalalStatus.SUPHELI,
            analyzeDe("Sonnenblumenöl, Basilikum 30 %, Cashewkerne, Parmigiano Reggiano Käse 5 %(tierisches Lab), Maisfasern, Molkenpulver, Salz").status
        )
    }

    @Test
    fun `GUT and GUENSTIG pesto naming Parmigiano Reggiano gU with only Lab is doubtful`() {
        // Exact OFF string, barcode 4311501104446, fetched 2026-11-02.
        assertEquals(
            HalalStatus.SUPHELI,
            analyzeDe("Sonnenblumenöl, 34 % Basilikum, gemahlene Cashewkerne, 5% Parmigiano Reggiano g.U. (Milch, Speisesalz, Lab), Molkeneiweißkonzentrat, Zucker, Speisesalz, 2% Pecorino Romano g.U. (Schafmilch, Speisesalz, Lab), natives Olivenöl extra, Säureregulator Milchsäure; Knoblauchpulver, Antioxidationsmittel: Ascorbinsäure").status
        )
    }

    @Test
    fun `kälberlab word is not treated as bare Lab`() {
        // Already covered by the animal rule; must not create a second, different flag.
        assertEquals(HalalStatus.SUPHELI, analyzeDe("Milch, Salz, Kälberlab").status)
    }
}
