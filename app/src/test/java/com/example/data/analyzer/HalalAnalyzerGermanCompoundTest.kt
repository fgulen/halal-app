package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import org.junit.Assert.assertEquals
import org.junit.Test

// German writes ingredients as compounds ("Süßmolkenpulver", "Schweineschmalz", "Rindergelatine"),
// so a whole-word-only keyword never matched them and half the user base got false Halal results.
class HalalAnalyzerGermanCompoundTest {

    private fun status(text: String, madhhab: Madhhab = Madhhab.UNSPECIFIED) =
        HalalAnalyzer.analyzeIngredientsText("x", text, language = AppLanguage.DE, madhhab = madhhab).status

    @Test fun `whey inside a compound is doubtful`() {
        assertEquals(HalalStatus.SUPHELI, status("Zucker, Kakaobutter, Süßmolkenpulver, Emulgator (Sojalecithin)."))
        assertEquals(HalalStatus.SUPHELI, status("Weizenmehl, Molkenerzeugnis, Salz."))
        assertEquals(HalalStatus.SUPHELI, status("Zucker, MOLKENPULVER, Kakao."))
    }

    @Test fun `hanafi clears compound whey like plain whey`() {
        assertEquals(HalalStatus.HELAL, status("Zucker, Süßmolkenpulver, Kakao.", Madhhab.HANAFI))
    }

    @Test fun `pork compounds are haram`() {
        assertEquals(HalalStatus.HARAM, status("Schweineschmalz, Salz, Gewürze."))
        assertEquals(HalalStatus.HARAM, status("Schweinespeck 60%, Salz."))
    }

    @Test fun `gelatine compounds are doubtful`() {
        assertEquals(HalalStatus.SUPHELI, status("Glukosesirup, Zucker, Rindergelatine, Säuerungsmittel."))
        assertEquals(HalalStatus.SUPHELI, status("Zucker, Speisegelatine, Aroma."))
    }

    @Test fun `free-from compounds are not flagged`() {
        assertEquals(HalalStatus.HELAL, status("Zucker, Glukosesirup, Fruchtsaft. Gelatinefrei."))
        assertEquals(HalalStatus.HELAL, status("Zucker, Kakaobutter, Vollmilchpulver. Molkenfrei."))
    }

    @Test fun `dairy and pig-shaped products are not whey or pork`() {
        assertEquals(HalalStatus.HELAL, status("Weizenmehl, Molkereibutter (25%), Zucker, Salz."))
        assertEquals(HalalStatus.HELAL, status("Schweineohren: Weizenmehl, Margarine (Palmfett, Rapsöl), Zucker."))
        assertEquals(HalalStatus.HELAL, status("Marzipanschwein: Mandeln, Zucker, Glukosesirup."))
        // ...while a real whey ingredient next to "Molkerei" is still caught.
        assertEquals(HalalStatus.SUPHELI, status("Molkereibutter, Zucker, Süßmolkenpulver."))
    }

    @Test fun `alcohol compounds are haram`() {
        assertEquals(HalalStatus.HARAM, status("Zucker, Eierlikör (15%), Weizenmehl."))
        assertEquals(HalalStatus.HARAM, status("Zucker, Rotwein (8%), Gewürze."))
        assertEquals(HalalStatus.HARAM, status("Kakao, Weinbrand 3%, Zucker."))
    }

    @Test fun `wine-derived acids and vinegar stay halal`() {
        assertEquals(HalalStatus.HELAL, status("Zucker, Säuerungsmittel Weinsäure, Backtriebmittel Weinstein."))
        assertEquals(HalalStatus.HELAL, status("Gurken, Wasser, Weinessig, Salz."))
        assertEquals(HalalStatus.HELAL, status("Wasser, Malz, alkoholfreies Bier."))
    }

    @Test fun `microbial rennet stays halal`() {
        // Bare "Lab" is deliberately not a keyword (it would hit "lab-grown" in English text),
        // so only the negation guard is pinned here.
        assertEquals(HalalStatus.HELAL, status("Milch, Salz, mikrobielles Lab, Milchsäurekulturen."))
    }
}
