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
  fun testRawEndpoints() {
    val client = OkHttpClient.Builder().build()
    val testCodes = listOf("8690504018040", "8000500003787", "8000500009659", "7622210019783", "9002490100070", "044000032029")
    for (code in testCodes) {
      val url = "https://world.openfoodfacts.org/api/v2/product/$code.json"
      val req = Request.Builder().url(url).header("User-Agent", "HalalCheckerApp/1.0").build()
      client.newCall(req).execute().use { resp ->
        val body = resp.body?.string() ?: ""
        val json = if (body.startsWith("{")) JSONObject(body) else JSONObject()
        val status = json.opt("status")
        val prod = json.optJSONObject("product")
        val name = prod?.optString("product_name") ?: prod?.optString("product_name_en") ?: prod?.optString("product_name_fr")
        println("RAW_CHECK: code=$code http=${resp.code} status=$status name=$name")
      }
    }
  }
}



