package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class OffResponse(
    @Json(name = "status") val status: Int?,
    @Json(name = "status_verbose") val statusVerbose: String?,
    @Json(name = "product") val product: OffProduct?
)

@JsonClass(generateAdapter = true)
data class OffProduct(
    @Json(name = "product_name") val productName: String?,
    @Json(name = "product_name_tr") val productNameTr: String?,
    @Json(name = "product_name_en") val productNameEn: String?,
    @Json(name = "product_name_de") val productNameDe: String?,
    @Json(name = "product_name_fr") val productNameFr: String?,
    @Json(name = "brands") val brands: String?,
    @Json(name = "categories") val categories: String?,
    @Json(name = "ingredients_text") val ingredientsText: String?,
    @Json(name = "ingredients_text_tr") val ingredientsTextTr: String?,
    @Json(name = "ingredients_text_en") val ingredientsTextEn: String?,
    @Json(name = "ingredients_text_de") val ingredientsTextDe: String?,
    @Json(name = "ingredients_text_fr") val ingredientsTextFr: String?,
    @Json(name = "additives_tags") val additivesTags: List<String>?,
    @Json(name = "ingredients_analysis_tags") val ingredientsAnalysisTags: List<String>?,
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "image_front_url") val imageFrontUrl: String?
)

interface OpenFoodFactsApi {
    @GET("api/v2/product/{barcode}.json")
    suspend fun getProductByBarcode(@Path("barcode") barcode: String): OffResponse

    companion object {
        private const val BASE_URL = "https://world.openfoodfacts.org/"

        fun create(): OpenFoodFactsApi {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("User-Agent", "HalalKontrolApp/1.0 (Android; Contact: support@halalkontrol.app)")
                        .build()
                    chain.proceed(request)
                }
                .build()

            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(OpenFoodFactsApi::class.java)
        }
    }
}
