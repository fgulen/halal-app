package com.example.data.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Test

class HealthPlainLabelsTest {

    @Test
    fun `every language has a plain Nutri-Score sentence for A to E`() {
        for (lang in AppLanguage.values()) {
            for (grade in listOf("A", "B", "C", "D", "E")) {
                assertFalse("$lang $grade", AppStrings.getNutriScorePlain(lang, grade).isBlank())
            }
        }
    }

    @Test
    fun `every language has a plain NOVA sentence for 1 to 4`() {
        for (lang in AppLanguage.values()) {
            for (group in 1..4) {
                assertFalse("$lang $group", AppStrings.getNovaPlain(lang, group).isBlank())
            }
        }
    }

    @Test
    fun `unknown grade or group returns empty text instead of crashing`() {
        assertEquals("", AppStrings.getNutriScorePlain(AppLanguage.TR, "Z"))
        assertEquals("", AppStrings.getNovaPlain(AppLanguage.TR, 0))
        assertEquals("", AppStrings.getNovaPlain(AppLanguage.TR, 5))
    }

    @Test
    fun `every language has a no-data line`() {
        for (lang in AppLanguage.values()) {
            assertFalse("$lang", AppStrings.getHealthGaugeNoData(lang).isBlank())
        }
    }
}
