package com.example.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SampleTranslationsTest {

    private val englishReason =
        "Contains no animal fats, gelatin or alcohol. Emulsifier is 100% plant-based soy/sunflower lecithin."
    private val englishCertificate = "Vegetarian Verified & Certified Clean"
    private val turkishReason = "Domuz türevi ve alkol içermez. %100 bitkisel soya lesitini."
    private val germanCertificate = "Vegetarisch / Keine tierischen Fette"

    @Test
    fun englishSampleTextIsTranslatedForTurkishGermanAndArabic() {
        assertNotEquals(englishReason, localizeSampleText(englishReason, AppLanguage.TR))
        assertNotEquals(englishReason, localizeSampleText(englishReason, AppLanguage.DE))
        assertNotEquals(englishReason, localizeSampleText(englishReason, AppLanguage.AR))
        assertNotEquals(englishCertificate, localizeSampleText(englishCertificate, AppLanguage.TR))
        assertNotEquals(englishCertificate, localizeSampleText(englishCertificate, AppLanguage.DE))
    }

    @Test
    fun englishTextStaysEnglishForEnglishScreens() {
        assertEquals(englishReason, localizeSampleText(englishReason, AppLanguage.EN))
    }

    @Test
    fun turkishSampleTextIsTranslatedForGermanScreens() {
        assertEquals(
            "Keine Schweinebestandteile und kein Alkohol. 100 % pflanzliches Sojalecithin.",
            localizeSampleText(turkishReason, AppLanguage.DE)
        )
    }

    @Test
    fun germanSampleTextIsTranslatedForTurkishScreens() {
        assertEquals(
            "Vejetaryen / hayvansal yağ yok",
            localizeSampleText(germanCertificate, AppLanguage.TR)
        )
    }

    @Test
    fun unknownTextIsReturnedUnchanged() {
        val custom = "Kullanıcının kendi notu"
        assertEquals(custom, localizeSampleText(custom, AppLanguage.DE))
        assertEquals(custom, localizeSampleText(custom, AppLanguage.AR))
    }
}
