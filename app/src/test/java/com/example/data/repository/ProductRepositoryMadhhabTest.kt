package com.example.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.AppLanguage
import com.example.data.model.Madhhab
import com.example.data.remote.OpenFoodFactsApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
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
          "product_name":"Test Cheese","brands":"Test",
          "ingredients_text":"milk, salt, animal rennet"}}
    """.trimIndent()

    private fun repository(): ProductRepository {
        val interceptor = Interceptor { chain ->
            val request = chain.request()
            val isOffFood = request.url.host == "world.openfoodfacts.org"
            if (isOffFood) offFoodCalls++
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(if (isOffFood) 200 else 404)
                .message("test")
                .body((if (isOffFood) cheeseJson else "{}").toResponseBody("application/json".toMediaType()))
                .build()
        }
        return ProductRepository(
            database.productDao(),
            OpenFoodFactsApi(OkHttpClient.Builder().addInterceptor(interceptor).build())
        )
    }

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
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

    @Test
    fun `cache hit under a different madhhab is re-analyzed`() = runTest {
        val repo = repository()
        repo.checkBarcode("1234567890123", AppLanguage.EN, Madhhab.SHAFII)
        val callsAfterFirst = offFoodCalls

        val again = repo.checkBarcode("1234567890123", AppLanguage.EN, Madhhab.HANAFI)

        assertEquals(callsAfterFirst + 1, offFoodCalls)
        assertEquals(Madhhab.HANAFI, again.madhhab)
    }

    @Test
    fun `photo analysis is stamped with the requested madhhab`() = runTest {
        val result = repository().analyzeIngredientsPhoto("Photo", "milk, salt, animal rennet", AppLanguage.EN, Madhhab.MALIKI)

        assertEquals(Madhhab.MALIKI, result.madhhab)
    }
}
