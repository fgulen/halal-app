package com.example.data.remote

import com.example.ui.components.nutriScoreSegmentIndex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HealthIndicatorTest {

    @Test
    fun `nutri-score accepts only plain A to E grades in any case`() {
        assertEquals("A", OpenFoodFactsApi.parseNutriScore("a"))
        assertEquals("E", OpenFoodFactsApi.parseNutriScore(" E "))
        assertEquals("C", OpenFoodFactsApi.parseNutriScore("c"))
    }

    @Test
    fun `nutri-score rejects OFF placeholders and junk`() {
        assertNull(OpenFoodFactsApi.parseNutriScore(""))
        assertNull(OpenFoodFactsApi.parseNutriScore("null"))
        assertNull(OpenFoodFactsApi.parseNutriScore("unknown"))
        assertNull(OpenFoodFactsApi.parseNutriScore("not-applicable"))
        assertNull(OpenFoodFactsApi.parseNutriScore("F"))
    }

    @Test
    fun `nova group accepts 1 to 4 only`() {
        assertEquals(1, OpenFoodFactsApi.parseNovaGroup("1"))
        assertEquals(4, OpenFoodFactsApi.parseNovaGroup("4"))
        assertEquals(3, OpenFoodFactsApi.parseNovaGroup("3.0"))
        assertNull(OpenFoodFactsApi.parseNovaGroup("3.5"))
        assertNull(OpenFoodFactsApi.parseNovaGroup("0"))
        assertNull(OpenFoodFactsApi.parseNovaGroup("5"))
        assertNull(OpenFoodFactsApi.parseNovaGroup("null"))
        assertNull(OpenFoodFactsApi.parseNovaGroup(""))
    }

    @Test
    fun `gauge runs from E on the left to A on the right`() {
        assertEquals(0, nutriScoreSegmentIndex("E"))
        assertEquals(2, nutriScoreSegmentIndex("c"))
        assertEquals(4, nutriScoreSegmentIndex("A"))
    }

    @Test
    fun `gauge has no segment for missing or unknown grades`() {
        assertNull(nutriScoreSegmentIndex(null))
        assertNull(nutriScoreSegmentIndex(""))
        assertNull(nutriScoreSegmentIndex("Z"))
        assertNull(nutriScoreSegmentIndex("AB"))
    }
}
