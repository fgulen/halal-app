package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import org.junit.Assert.assertEquals
import org.junit.Test

// Arabic ingredient text had no keywords at all: "جيلاتين خنزير" (pork gelatine) came out Halal.
// Arabic also attaches the article/conjunctions to the noun ("الخنزير", "والكحول"), which a plain
// whole-word match never sees.
class HalalAnalyzerArabicTest {

    private fun status(text: String, madhhab: Madhhab = Madhhab.UNSPECIFIED) =
        HalalAnalyzer.analyzeIngredientsText("x", text, language = AppLanguage.AR, madhhab = madhhab).status

    @Test fun `pork is haram with or without the article`() {
        assertEquals(HalalStatus.HARAM, status("شراب الجلوكوز، سكر، جيلاتين خنزير، حمض الستريك."))
        assertEquals(HalalStatus.HARAM, status("لحم، ملح، دهن الخنزير."))
    }

    @Test fun `alcohol is haram`() {
        assertEquals(HalalStatus.HARAM, status("سكر، دقيق القمح، بيض، روم (4%)."))
        assertEquals(HalalStatus.HARAM, status("ماء، سكر، والكحول، نكهة."))
        assertEquals(HalalStatus.HARAM, status("عنب، نبيذ أحمر، سكر."))
    }

    @Test fun `alcohol-free and wine vinegar stay halal`() {
        assertEquals(HalalStatus.HELAL, status("ماء، سكر، نكهة طبيعية. خالي من الكحول."))
        assertEquals(HalalStatus.HELAL, status("خيار، ماء، خل النبيذ، ملح."))
    }

    @Test fun `gelatine of unknown source is doubtful`() {
        assertEquals(HalalStatus.SUPHELI, status("سكر، الجيلاتين البقري، نكهة."))
    }

    @Test fun `whey is doubtful and hanafi clears it`() {
        assertEquals(HalalStatus.SUPHELI, status("سكر، زبدة الكاكاو، مسحوق مصل اللبن."))
        assertEquals(HalalStatus.HELAL, status("سكر، زبدة الكاكاو، مسحوق مصل اللبن.", Madhhab.HANAFI))
    }

    @Test fun `rennet is doubtful in any alif spelling`() {
        assertEquals(HalalStatus.SUPHELI, status("حليب، ملح، إنفحة حيوانية."))
        assertEquals(HalalStatus.SUPHELI, status("حليب، ملح، انفحة."))
        assertEquals(HalalStatus.HELAL, status("حليب، ملح، إنفحة.", Madhhab.HANAFI))
    }

    @Test fun `plain products stay halal`() {
        assertEquals(HalalStatus.HELAL, status("دقيق القمح، زيت دوار الشمس، ملح، خميرة."))
    }
}
