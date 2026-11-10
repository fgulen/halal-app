package com.example.data.repository

import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.createInMemoryAppDatabase
import com.example.data.remote.offMockClient
import com.example.data.local.ProductEntity
import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.data.remote.OpenFoodFactsApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ProductRepositoryMadhhabTest {

    private lateinit var database: AppDatabase
    private var offFoodCalls = 0

    private val cheeseJson = """
        {"status":1,"code":"1234567890123","product":{
          "product_name":"Test Cheese","brands":"Test","nutriscore_grade":"c",
          "ingredients_text":"milk, salt, animal rennet"}}
    """.trimIndent()

    private fun repository(offBody: String = cheeseJson, offNetworkError: Boolean = false): ProductRepository {
        return ProductRepository(
            database.productDao(),
            OpenFoodFactsApi(offMockClient(offFoodBody = offBody, onOffFoodRequest = { offFoodCalls++ }, failOffFood = offNetworkError))
        )
    }

    @Before
    fun setUp() {
        database = createInMemoryAppDatabase(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `live result and its history row carry the requested madhhab`() = runTest {
        val repo = repository()

        val result = repo.checkBarcode("1234567890123", AppLanguage.EN, Madhhab.HANAFI)

        assertEquals(Madhhab.HANAFI, result.madhhab)
        assertEquals(Madhhab.HANAFI, repo.getScanHistory().first().first().madhhab)
    }

    @Test
    fun `cache hit under the same madhhab is reused without a network call`() = runTest {
        val repo = repository()
        repo.checkBarcode("1234567890123", AppLanguage.EN, Madhhab.SHAFII)
        val callsAfterFirst = offFoodCalls

        val again = repo.checkBarcode("1234567890123", AppLanguage.EN, Madhhab.SHAFII)

        assertEquals(callsAfterFirst, offFoodCalls)
        assertEquals(Madhhab.SHAFII, again.madhhab)
    }

    // A cached product without health data (cached before v7, or no Nutri-Score at OFF) is fetched
    // again when online, so pre-v7 rows pick up their Nutri-Score/NOVA.
    @Test
    fun `cache hit without health data is refetched when online`() = runTest {
        val repo = repository()
        database.productDao().insertProduct(
            ProductEntity(
                barcode = "1234567890123", name = "Old", brand = "Test", category = "Cheese",
                status = HalalStatus.HELAL, halalCertificate = null, harmfulOrSuspiciousIngredients = emptyList(),
                allIngredients = emptyList(), reasonOrDetails = "", alternatives = emptyList(), imageUrl = null,
                language = AppLanguage.EN, madhhab = Madhhab.SHAFII, nutriScore = null, novaGroup = null
            )
        )

        val result = repo.checkBarcode("1234567890123", AppLanguage.EN, Madhhab.SHAFII)

        assertEquals(1, offFoodCalls)
        assertEquals("C", result.nutriScore)
    }

    @Test
    fun `cache hit under a different madhhab is re-analyzed`() = runTest {
        val repo = repository()
        repo.checkBarcode("1234567890123", AppLanguage.EN, Madhhab.SHAFII)
        val callsAfterFirst = offFoodCalls

        val again = repo.checkBarcode("1234567890123", AppLanguage.EN, Madhhab.HANAFI)

        assertEquals(callsAfterFirst + 1, offFoodCalls)
        assertEquals(Madhhab.HANAFI, again.madhhab)
    }

    private fun cachedWithoutHealth(): ProductEntity = ProductEntity(
        barcode = "1234567890123", name = "Cached Cheese", brand = "Cached", category = "Cheese",
        status = HalalStatus.HELAL, halalCertificate = null, harmfulOrSuspiciousIngredients = emptyList(),
        allIngredients = emptyList(), reasonOrDetails = "", alternatives = emptyList(), imageUrl = null,
        language = AppLanguage.EN, madhhab = Madhhab.SHAFII, nutriScore = null, novaGroup = null
    )

    // Regression: a network error with a same-language cached row used to throw
    // NetworkUnavailableException instead of returning the cached product.
    @Test
    fun `offline with a same-language cached row returns the cached product`() = runTest {
        database.productDao().insertProduct(cachedWithoutHealth())

        val result = repository(offNetworkError = true).checkBarcode("1234567890123", AppLanguage.EN, Madhhab.SHAFII)

        assertEquals("Cached Cheese", result.name)
    }

    // OFF has nothing for the barcode: the cached row must win over the name-search fallback.
    @Test
    fun `OFF has nothing for the barcode and the cached row is kept`() = runTest {
        database.productDao().insertProduct(cachedWithoutHealth())

        val result = repository(offBody = """{"status":0,"status_verbose":"product not found"}""")
            .checkBarcode("1234567890123", AppLanguage.EN, Madhhab.SHAFII)

        assertEquals("Cached Cheese", result.name)
    }

    @Test
    fun `photo analysis is stamped with the requested madhhab`() = runTest {
        val result = repository().analyzeIngredientsPhoto("Photo", "milk, salt, animal rennet", AppLanguage.EN, Madhhab.MALIKI)

        assertEquals(Madhhab.MALIKI, result.madhhab)
    }
}
