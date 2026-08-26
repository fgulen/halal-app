package com.example.data.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class OffProduct(
    val productName: String? = null,
    val productNameEn: String? = null,
    val productNameDe: String? = null,
    val productNameFr: String? = null,
    val productNameEs: String? = null,
    val productNameTr: String? = null,
    val productNameAr: String? = null,
    val brands: String? = null,
    val categories: String? = null,
    val countriesTags: List<String>? = null,
    val labelsTags: List<String>? = null,
    val ingredientsText: String? = null,
    val ingredientsTextEn: String? = null,
    val ingredientsTextDe: String? = null,
    val ingredientsTextFr: String? = null,
    val ingredientsTextEs: String? = null,
    val ingredientsTextTr: String? = null,
    val ingredientsTextAr: String? = null,
    val additivesTags: List<String>? = null,
    val ingredientsAnalysisTags: List<String>? = null,
    val imageUrl: String? = null,
    val imageFrontUrl: String? = null,
    val imageFrontSmallUrl: String? = null,
    val imageSmallUrl: String? = null,
    val imageThumbUrl: String? = null,
    val imageFrontThumbUrl: String? = null
)

data class OffResponse(
    val status: Int?,
    val statusVerbose: String?,
    val code: String?,
    val product: OffProduct?
)

class OpenFoodFactsApi(
    private val client: OkHttpClient = createOkHttpClient()
) {

    suspend fun getProductByBarcode(barcode: String): OffResponse = withContext(Dispatchers.IO) {
        val cleanedBarcode = barcode.trim()
        val digitsOnly = cleanedBarcode.filter { it.isDigit() }
        val targetBarcode = if (digitsOnly.isNotEmpty()) digitsOnly else cleanedBarcode

        if (targetBarcode.isBlank()) {
            return@withContext OffResponse(status = 0, statusVerbose = "empty barcode", code = barcode, product = null)
        }

        // Generate barcode candidate variations (e.g. leading zero padding for UPC, or removing leading zero)
        val barcodeCandidates = buildList {
            add(targetBarcode)
            if (targetBarcode.length == 12) {
                add("0$targetBarcode") // 12-digit UPC to 13-digit EAN
            } else if (targetBarcode.length == 13 && targetBarcode.startsWith("0")) {
                add(targetBarcode.substring(1)) // 13-digit to 12-digit UPC
            }
            if (cleanedBarcode != targetBarcode) {
                add(cleanedBarcode)
            }
        }.distinct()

        val primaryCandidate = barcodeCandidates.first()
        val urlsToTry = buildList {
            // v2 API endpoints
            for (candidate in barcodeCandidates) {
                add("https://world.openfoodfacts.org/api/v2/product/$candidate.json")
                add("https://world.openfoodfacts.org/api/v0/product/$candidate.json")
                add("https://de.openfoodfacts.org/api/v2/product/$candidate.json")
                add("https://fr.openfoodfacts.org/api/v2/product/$candidate.json")
                add("https://tr.openfoodfacts.org/api/v2/product/$candidate.json")
                add("https://us.openfoodfacts.org/api/v2/product/$candidate.json")
                add("https://world.openbeautyfacts.org/api/v2/product/$candidate.json")
            }
        }

        for (url in urlsToTry) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "HalalGlobalFoodScanner - Android - Version 1.0 (contact: support@halalglobal.app)")
                    .header("Accept", "application/json")
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val bodyString = response.body?.string()
                        if (!bodyString.isNullOrBlank()) {
                            val json = JSONObject(bodyString)
                            val parsed = parseOffResponse(json, primaryCandidate)
                            if (parsed != null && (parsed.status == 1 || parsed.product != null)) {
                                return@withContext parsed
                            }
                        }
                    }
                }
            } catch (_: Exception) {
                // try next
            }
        }

        // Fallback: search by barcode term using search v2 API
        try {
            val searchResults = searchProductsByName(targetBarcode)
            if (searchResults.isNotEmpty()) {
                val (code, product) = searchResults.first()
                return@withContext OffResponse(status = 1, statusVerbose = "found via search", code = code, product = product)
            }
        } catch (_: Exception) {
            // fall through
        }

        OffResponse(status = 0, statusVerbose = "product not found", code = targetBarcode, product = null)
    }

    suspend fun searchProductsByName(query: String): List<Pair<String, OffProduct>> = withContext(Dispatchers.IO) {
        val cleanedQuery = query.trim()
        if (cleanedQuery.isBlank()) return@withContext emptyList()

        val results = mutableListOf<Pair<String, OffProduct>>()
        val encodedQuery = java.net.URLEncoder.encode(cleanedQuery, "UTF-8")
        val searchUrls = listOf(
            "https://world.openfoodfacts.org/api/v2/search?search_terms=$encodedQuery&page_size=10&fields=code,_id,product_name,product_name_en,product_name_de,product_name_fr,product_name_tr,product_name_ar,brands,categories,ingredients_text,ingredients_text_en,ingredients_text_de,ingredients_text_fr,ingredients_text_tr,ingredients_text_ar,additives_tags,ingredients_analysis_tags,image_front_url,image_url,image_front_small_url,image_small_url,selected_images",
            "https://de.openfoodfacts.org/api/v2/search?search_terms=$encodedQuery&page_size=8",
            "https://tr.openfoodfacts.org/api/v2/search?search_terms=$encodedQuery&page_size=8"
        )

        for (searchUrl in searchUrls) {
            try {
                val request = Request.Builder()
                    .url(searchUrl)
                    .header("User-Agent", "HalalGlobalFoodScanner - Android - Version 1.0 (contact: support@halalglobal.app)")
                    .header("Accept", "application/json")
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val bodyString = response.body?.string()
                        if (!bodyString.isNullOrBlank()) {
                            val json = JSONObject(bodyString)
                            val productsArray = json.optJSONArray("products")
                            if (productsArray != null) {
                                for (i in 0 until productsArray.length()) {
                                    val prodJson = productsArray.optJSONObject(i) ?: continue
                                    val code = prodJson.optString("code", prodJson.optString("_id", ""))
                                    val offProd = parseOffProduct(prodJson)
                                    if (code.isNotBlank() && offProd != null && results.none { it.first == code }) {
                                        results.add(Pair(code, offProd))
                                    }
                                }
                            }
                        }
                    }
                }
                if (results.isNotEmpty()) break
            } catch (_: Exception) {
                // try next
            }
        }

        results
    }

    private fun parseOffResponse(json: JSONObject, fallbackCode: String): OffResponse? {
        val statusRaw = json.opt("status")
        val statusInt = when (statusRaw) {
            is Number -> statusRaw.toInt()
            is String -> statusRaw.toIntOrNull() ?: if (statusRaw.equals("success", ignoreCase = true)) 1 else 0
            else -> 0
        }
        val statusVerbose = json.optString("status_verbose", "")
        val code = json.optString("code", fallbackCode)
        val productJson = json.optJSONObject("product")

        val product = if (productJson != null) {
            parseOffProduct(productJson)
        } else null

        return OffResponse(
            status = if (product != null) 1 else statusInt,
            statusVerbose = statusVerbose,
            code = code,
            product = product
        )
    }

    private fun parseOffProduct(json: JSONObject): OffProduct? {
        val name = json.optString("product_name").takeIf { it.isNotBlank() }
            ?: json.optString("product_name_en").takeIf { it.isNotBlank() }
            ?: json.optString("product_name_de").takeIf { it.isNotBlank() }
            ?: json.optString("product_name_fr").takeIf { it.isNotBlank() }
            ?: json.optString("generic_name").takeIf { it.isNotBlank() }

        // If completely empty JSON object, return null
        if (name.isNullOrBlank() && !json.has("ingredients_text") && !json.has("brands")) {
            return null
        }

        fun extractStringList(key: String): List<String>? {
            val opt = json.opt(key) ?: return null
            return when (opt) {
                is JSONArray -> {
                    val list = mutableListOf<String>()
                    for (i in 0 until opt.length()) {
                        val str = opt.optString(i)
                        if (str.isNotBlank()) list.add(str)
                    }
                    list.takeIf { it.isNotEmpty() }
                }
                is String -> {
                    if (opt.isBlank()) null
                    else opt.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                }
                else -> null
            }
        }

        fun extractBestImageUrl(): String? {
            fun cleanUrl(raw: String?): String? {
                if (raw.isNullOrBlank()) return null
                val trimmed = raw.trim()
                return when {
                    trimmed.startsWith("http://") -> trimmed.replaceFirst("http://", "https://")
                    trimmed.startsWith("https://") -> trimmed
                    trimmed.startsWith("//") -> "https:$trimmed"
                    else -> trimmed
                }
            }

            // 1. Direct fields
            val direct = cleanUrl(json.optString("image_front_url").takeIf { it.isNotBlank() })
                ?: cleanUrl(json.optString("image_url").takeIf { it.isNotBlank() })
                ?: cleanUrl(json.optString("image_front_small_url").takeIf { it.isNotBlank() })
                ?: cleanUrl(json.optString("image_small_url").takeIf { it.isNotBlank() })
                ?: cleanUrl(json.optString("image_thumb_url").takeIf { it.isNotBlank() })
                ?: cleanUrl(json.optString("image_front_thumb_url").takeIf { it.isNotBlank() })
            if (!direct.isNullOrBlank()) return direct

            // 2. selected_images -> front -> display / small / thumb
            val selected = json.optJSONObject("selected_images")
            if (selected != null) {
                val front = selected.optJSONObject("front") ?: selected.optJSONObject("ingredients")
                if (front != null) {
                    val display = front.optJSONObject("display") ?: front.optJSONObject("small") ?: front.optJSONObject("thumb")
                    if (display != null) {
                        val langs = listOf("de", "en", "fr", "tr", "ar", "it", "es", "nl", "pl", "ru")
                        for (lang in langs) {
                            val u = cleanUrl(display.optString(lang).takeIf { it.isNotBlank() })
                            if (!u.isNullOrBlank()) return u
                        }
                        val keys = display.keys()
                        while (keys.hasNext()) {
                            val k = keys.next()
                            val u = cleanUrl(display.optString(k).takeIf { it.isNotBlank() })
                            if (!u.isNullOrBlank()) return u
                        }
                    }
                }
            }

            // 3. Fallback to Open Food Facts CDN split barcode path
            val rawCode = json.optString("code").takeIf { it.isNotBlank() }
            if (!rawCode.isNullOrBlank()) {
                val digits = rawCode.trim().filter { it.isDigit() }
                if (digits.isNotEmpty()) {
                    return if (digits.length <= 8) {
                        "https://images.openfoodfacts.org/images/products/$digits/1.400.jpg"
                    } else {
                        val c1 = digits.take(3)
                        val c2 = digits.drop(3).take(3)
                        val c3 = digits.drop(6).take(3)
                        val c4 = digits.drop(9)
                        "https://images.openfoodfacts.org/images/products/$c1/$c2/$c3/$c4/1.400.jpg"
                    }
                }
            }

            return null
        }

        val resolvedImageUrl = extractBestImageUrl()

        return OffProduct(
            productName = name,
            productNameEn = json.optString("product_name_en").takeIf { it.isNotBlank() },
            productNameDe = json.optString("product_name_de").takeIf { it.isNotBlank() },
            productNameFr = json.optString("product_name_fr").takeIf { it.isNotBlank() },
            productNameEs = json.optString("product_name_es").takeIf { it.isNotBlank() },
            productNameTr = json.optString("product_name_tr").takeIf { it.isNotBlank() },
            productNameAr = json.optString("product_name_ar").takeIf { it.isNotBlank() },
            brands = json.optString("brands").takeIf { it.isNotBlank() } ?: json.optString("brand_owner").takeIf { it.isNotBlank() },
            categories = json.optString("categories").takeIf { it.isNotBlank() },
            countriesTags = extractStringList("countries_tags") ?: extractStringList("countries"),
            labelsTags = extractStringList("labels_tags") ?: extractStringList("labels"),
            ingredientsText = json.optString("ingredients_text").takeIf { it.isNotBlank() },
            ingredientsTextEn = json.optString("ingredients_text_en").takeIf { it.isNotBlank() },
            ingredientsTextDe = json.optString("ingredients_text_de").takeIf { it.isNotBlank() },
            ingredientsTextFr = json.optString("ingredients_text_fr").takeIf { it.isNotBlank() },
            ingredientsTextEs = json.optString("ingredients_text_es").takeIf { it.isNotBlank() },
            ingredientsTextTr = json.optString("ingredients_text_tr").takeIf { it.isNotBlank() },
            ingredientsTextAr = json.optString("ingredients_text_ar").takeIf { it.isNotBlank() },
            additivesTags = extractStringList("additives_tags") ?: extractStringList("additives_original_tags"),
            ingredientsAnalysisTags = extractStringList("ingredients_analysis_tags"),
            imageUrl = resolvedImageUrl,
            imageFrontUrl = resolvedImageUrl,
            imageFrontSmallUrl = resolvedImageUrl,
            imageSmallUrl = resolvedImageUrl,
            imageThumbUrl = resolvedImageUrl,
            imageFrontThumbUrl = resolvedImageUrl
        )
    }

    companion object {
        fun createOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(12, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .build()
        }

        fun create(): OpenFoodFactsApi {
            return OpenFoodFactsApi(createOkHttpClient())
        }
    }
}

