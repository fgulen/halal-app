package com.example

import com.example.data.analyzer.HalalAnalyzer
import com.example.data.local.InitialData
import com.example.data.model.AppLanguage
import com.example.data.remote.OpenFoodFactsApi
import com.example.data.repository.ProductRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

  @Test
  fun testVerifyAllSampleImageUrls() {
    val client = OkHttpClient.Builder()
      .connectTimeout(12, TimeUnit.SECONDS)
      .readTimeout(12, TimeUnit.SECONDS)
      .followRedirects(true)
      .build()

    for (sample in InitialData.sampleProducts) {
      val url = sample.imageUrl
      if (!url.isNullOrBlank()) {
        try {
          val req = Request.Builder()
            .url(url)
            .header("User-Agent", "HalalGlobalFoodScanner/1.0 (Android; Linux)")
            .head()
            .build()
          client.newCall(req).execute().use { resp ->
            println("IMG_URL_CHECK: barcode=${sample.barcode} name=${sample.name} code=${resp.code} url=$url")
          }
        } catch (e: Exception) {
          println("IMG_URL_CHECK_ERR: barcode=${sample.barcode} err=${e.message}")
        }
      }
    }
  }

  @Test
  fun testLiveOpenFoodFactsBarcodes() {
    val api = OpenFoodFactsApi.create()

    val testBarcodes = listOf(
      "3017620422003", // Nutella 400g FR
      "4008400404127", // Nutella DE
      "4001686301265", // Haribo Goldbären
      "5449000000996", // Coca-Cola Original 330ml
      "7622210449283", // Milka Daim
      "5000159461122", // Skittles
      "5410126006957", // Lotus Biscoff
      "5000159407236", // Mars
      "8715700421384"  // Heinz
    )

    for (code in testBarcodes) {
      val resp = kotlinx.coroutines.runBlocking {
        api.getProductByBarcode(code)
      }
      println("API_TEST: code=$code status=${resp.status} name=${resp.product?.productName} img=${resp.product?.imageUrl}")
    }
  }
}



