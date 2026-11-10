package com.example.data.remote

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// org.json'dan kotlinx.serialization'a geçişte eski ayrıştırma kurallarının korunduğunu sabitler:
// JSON null -> "null" metni (optString), sayısal nova_group ("4.0"), "//" ile başlayan görsel URL.
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class OffParsingCompatibilityTest {

    private val body = """
        {"status":1,"code":"1234567890123","product":{
          "product_name":"Test Bar","brands":"Brand",
          "nutriscore_grade":null,"nova_group":4.0,
          "image_front_url":"//img.example.org/a.jpg",
          "ingredients_text":"sugar, gelatin",
          "countries_tags":["en:turkey","null"],
          "additives_tags":"en:e441,en:e330"}}
    """.trimIndent()

    @Test
    fun `OFF json quirks parse the same way the org json parser did`() = runTest {
        val api = OpenFoodFactsApi(offMockClient(offFoodBody = body))

        val product = api.getProductByBarcode("1234567890123").product!!

        assertEquals("Test Bar", product.productName)
        assertNull(product.nutriScore)
        assertEquals(4, product.novaGroup)
        assertEquals("https://img.example.org/a.jpg", product.imageUrl)
        assertEquals(listOf("en:turkey", "null"), product.countriesTags)
        assertEquals(listOf("en:e441", "en:e330"), product.additivesTags)
    }
}
