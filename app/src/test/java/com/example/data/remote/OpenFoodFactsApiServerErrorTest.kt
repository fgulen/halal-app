package com.example.data.remote

import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.createInMemoryAppDatabase
import com.example.data.repository.ProductRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// Regression for the Tuc (5410041001204) incident on 2026-10-07: OFF's food endpoints returned a
// transient HTTP 502 while openbeautyfacts returned 404, and the app reported the product as
// "not found" and wrote that verdict into scan history instead of treating it as a server outage.
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class OpenFoodFactsApiServerErrorTest {

    private fun clientAnswering(offFoodCode: Int, offFoodBody: String = "{}") =
        offMockClient(offFoodStatus = offFoodCode, offFoodBody = offFoodBody)

    @Test
    fun `OFF 502 with beauty-facts 404 is a network error, not product not found`() = runTest {
        val api = OpenFoodFactsApi(clientAnswering(offFoodCode = 502))

        val response = api.getProductByBarcode("5410041001204")

        assertNull(response.product)
        assertEquals("network error", response.statusVerbose)
    }

    @Test
    fun `OFF 429 rate limit is a network error`() = runTest {
        val api = OpenFoodFactsApi(clientAnswering(offFoodCode = 429))

        val response = api.getProductByBarcode("5410041001204")

        assertEquals("network error", response.statusVerbose)
    }

    @Test
    fun `OFF 404 is still a definite product not found`() = runTest {
        val api = OpenFoodFactsApi(
            clientAnswering(offFoodCode = 404, offFoodBody = """{"status":0,"status_verbose":"product not found"}""")
        )

        val response = api.getProductByBarcode("5410041001204")

        assertEquals("product not found", response.statusVerbose)
    }

    @Test
    fun `repository surfaces OFF 502 as NetworkUnavailable and records no history`() = runTest {
        val database = createInMemoryAppDatabase(ApplicationProvider.getApplicationContext())
        try {
            val repository = ProductRepository(
                database.productDao(),
                OpenFoodFactsApi(clientAnswering(offFoodCode = 502))
            )

            try {
                repository.checkBarcode("5410041001204")
                fail("expected NetworkUnavailableException for an OFF 502")
            } catch (e: ProductRepository.NetworkUnavailableException) {
                // expected
            }

            assertTrue(repository.getScanHistory().first().isEmpty())
        } finally {
            database.close()
        }
    }
}
