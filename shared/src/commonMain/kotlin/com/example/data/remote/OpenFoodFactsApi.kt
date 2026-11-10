package com.example.data.remote

import com.example.data.util.logDebug
import com.example.data.util.logWarn
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.http.encodeURLParameter
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlin.math.floor

private const val TAG = "OpenFoodFactsApi"
private const val USER_AGENT = "HalalGlobalFoodScanner - Android - Version 1.0 (contact: support@halalglobal.app)"

data class OffResponse(
    val status: Int?,
    val statusVerbose: String?,
    val code: String?,
    val product: OffProduct?
)

// org.json'dan (JSONObject/JSONArray) geçişte aynı ayrıştırma davranışını korumak için yardımcılar.
// Önemli farklar: eksik alan "" döner; JSON null "null" metni olur (org.json öyleydi); sayılar
// metne çevrilir. Ayrıştırıcı bu kuralların hiçbirini değiştirmez.
private fun JsonElement.orgString(): String = when (this) {
    is JsonNull -> "null"
    is JsonPrimitive -> content
    else -> toString()
}

private fun JsonObject.optString(key: String, fallback: String = ""): String =
    this[key]?.orgString() ?: fallback

private fun JsonObject.optObject(key: String): JsonObject? = this[key] as? JsonObject

class OpenFoodFactsApi(
    private val client: HttpClient = createOffHttpClient()
) {

    suspend fun getProductByBarcode(barcode: String): OffResponse {
        val cleanedBarcode = barcode.trim()
        val digitsOnly = cleanedBarcode.filter { it.isDigit() }
        val targetBarcode = if (digitsOnly.isNotEmpty()) digitsOnly else cleanedBarcode

        if (targetBarcode.isBlank()) {
            return OffResponse(status = 0, statusVerbose = "empty barcode", code = barcode, product = null)
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
        // world.openfoodfacts.org serves the same shared database as the country subdomains
        // (tr/de/fr/us), so querying those separately never finds anything "world" didn't.
        val urlsToTry = buildList {
            for (candidate in barcodeCandidates) {
                add("https://world.openfoodfacts.org/api/v2/product/$candidate.json")
                add("https://world.openfoodfacts.org/api/v0/product/$candidate.json")
                add("https://world.openbeautyfacts.org/api/v2/product/$candidate.json")
            }
        }

        // Whether an Open Food Facts *food* endpoint gave a definite answer (2xx, or 404) - the
        // only basis for reporting "product not found" below. Any HTTP response used to count,
        // so on 2026-10-07 an OFF outage (v2 + v0 both HTTP 502) plus openbeautyfacts' routine
        // 404 was reported as "not found" and recorded into history as an unregistered product.
        // 5xx/429 (outage, rate limit) and openbeautyfacts responses therefore never count.
        var offGaveDefiniteAnswer = false

        // A response with SOME data (name/brand/image) but no ingredients is what produces the
        // "image found, content not found" result in the UI. urlsToTry deliberately queries the
        // same barcode across multiple endpoints (v2, the older/less complete v0, openbeautyfacts)
        // and multiple digit-padding variants, precisely because different ones can have different
        // completeness for the same product. Returning on the FIRST endpoint with any usable field
        // let an early partial hit (e.g. v0 or a timed-out-then-retried v2) shadow a fuller record
        // one of the later URLs would have had. Keep the first partial match in reserve instead of
        // returning it immediately, so every remaining URL still gets a chance to find a response
        // that actually has ingredients - only fall back to the partial one once nothing fuller
        // turned up anywhere.
        var bestPartialMatch: OffResponse? = null

        fun hasIngredientData(product: OffProduct?): Boolean {
            if (product == null) return false
            return !product.ingredientsText.isNullOrBlank() ||
                !product.ingredientsTextEn.isNullOrBlank() ||
                !product.ingredientsTextDe.isNullOrBlank() ||
                !product.ingredientsTextFr.isNullOrBlank() ||
                !product.ingredientsTextEs.isNullOrBlank() ||
                !product.ingredientsTextTr.isNullOrBlank() ||
                !product.ingredientsTextAr.isNullOrBlank() ||
                !product.additivesTags.isNullOrEmpty()
        }

        for (url in urlsToTry) {
            try {
                val response = client.get(url) {
                    headers {
                        append(HttpHeaders.UserAgent, USER_AGENT)
                        append(HttpHeaders.Accept, "application/json")
                    }
                }
                if (Url(url).host == "world.openfoodfacts.org" &&
                    (response.status.isSuccess() || response.status.value == 404)
                ) {
                    offGaveDefiniteAnswer = true
                }
                logDebug(TAG, "GET $url -> HTTP ${response.status.value}")
                if (response.status.isSuccess()) {
                    val bodyString = response.bodyAsText()
                    if (bodyString.isNotBlank()) {
                        val json = parseJsonObject(bodyString)
                        val parsed = parseOffResponse(json, primaryCandidate)
                        val product = parsed?.product
                        // Accept the product as soon as OFF has ANY usable data for it.
                        // Requiring a non-blank product_name rejected real entries that only
                        // have brand/ingredients/image data filled in, sending them into the
                        // "not found" path with no image and no ingredients.
                        val hasUsableData = product != null && (
                            !product.productName.isNullOrBlank() ||
                                !product.brands.isNullOrBlank() ||
                                !product.ingredientsText.isNullOrBlank() ||
                                !product.imageUrl.isNullOrBlank()
                            )
                        if (parsed != null && hasUsableData) {
                            if (hasIngredientData(product)) {
                                logDebug(TAG, "Matched barcode=$primaryCandidate name=${product?.productName} image=${product?.imageUrl}")
                                return parsed
                            }
                            if (bestPartialMatch == null) {
                                logDebug(TAG, "Partial match (no ingredients yet) barcode=$primaryCandidate name=${product?.productName} - still checking remaining endpoints")
                                bestPartialMatch = parsed
                            }
                        }
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logWarn(TAG, "Request failed for $url: ${e.message}")
                // try next
            }
        }

        // Fallback: search by barcode term using search v2 API only if it actually matches
        if (targetBarcode.length >= 4) {
            try {
                val searchResults = searchProductsByName(targetBarcode)
                val exactMatch = searchResults.firstOrNull { it.first == targetBarcode || it.first == primaryCandidate }
                if (exactMatch != null && hasIngredientData(exactMatch.second)) {
                    return OffResponse(status = 1, statusVerbose = "found via search", code = exactMatch.first, product = exactMatch.second)
                }
                if (exactMatch != null && bestPartialMatch == null) {
                    bestPartialMatch = OffResponse(status = 1, statusVerbose = "found via search", code = exactMatch.first, product = exactMatch.second)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logWarn(TAG, "Search fallback failed: ${e.message}")
            }
        }

        // No endpoint/candidate had ingredients, but one had name/brand/image data - better to
        // show that (with the "photo analysis" fallback CTA) than a bare "not found" screen.
        bestPartialMatch?.let { return it }

        val verbose = if (offGaveDefiniteAnswer) "product not found" else "network error"
        logWarn(TAG, "Barcode $targetBarcode not found on Open Food Facts after ${urlsToTry.size} attempts ($verbose)")
        return OffResponse(status = 0, statusVerbose = verbose, code = targetBarcode, product = null)
    }

    suspend fun searchProductsByName(query: String): List<Pair<String, OffProduct>> {
        val cleanedQuery = query.trim()
        if (cleanedQuery.isBlank()) return emptyList()

        val results = mutableListOf<Pair<String, OffProduct>>()
        // URLEncoder ile aynı: boşluk '+' olur.
        val encodedQuery = cleanedQuery.encodeURLParameter(spaceToPlus = true)
        // de/tr subdomains share the same underlying database as world and were dropped here
        // for the same reason as getProductByBarcode: they can't find anything world can't,
        // and their search endpoint doesn't request the same `fields`, so results were inconsistent.
        val searchUrls = listOf(
            "https://world.openfoodfacts.org/api/v2/search?search_terms=$encodedQuery&page_size=15&fields=code,_id,product_name,product_name_en,product_name_de,product_name_fr,product_name_tr,product_name_ar,brands,categories,ingredients_text,ingredients_text_en,ingredients_text_de,ingredients_text_fr,ingredients_text_tr,ingredients_text_ar,additives_tags,ingredients_analysis_tags,nutriscore_grade,nova_group,image_front_url,image_url,image_front_small_url,image_small_url,selected_images,images"
        )

        for (searchUrl in searchUrls) {
            try {
                val response = client.get(searchUrl) {
                    headers {
                        append(HttpHeaders.UserAgent, USER_AGENT)
                        append(HttpHeaders.Accept, "application/json")
                    }
                }
                if (response.status.isSuccess()) {
                    val bodyString = response.bodyAsText()
                    if (bodyString.isNotBlank()) {
                        val json = parseJsonObject(bodyString)
                        val productsArray = json["products"] as? JsonArray
                        if (productsArray != null) {
                            val queryLower = cleanedQuery.lowercase()
                            val isNumericQuery = cleanedQuery.all { it.isDigit() }
                            for (element in productsArray) {
                                val prodJson = element as? JsonObject ?: continue
                                val code = prodJson.optString("code", prodJson.optString("_id", ""))
                                val offProd = parseOffProduct(prodJson)
                                if (code.isNotBlank() && offProd != null && !offProd.productName.isNullOrBlank() && results.none { it.first == code }) {
                                    val pName = (offProd.productName ?: "").lowercase()
                                    val pBrand = (offProd.brands ?: "").lowercase()
                                    val pCat = (offProd.categories ?: "").lowercase()

                                    // Verify relevance
                                    if (isNumericQuery) {
                                        if (code.contains(cleanedQuery)) {
                                            results.add(Pair(code, offProd))
                                        }
                                    } else {
                                        if (pName.contains(queryLower) || pBrand.contains(queryLower) || pCat.contains(queryLower) || queryLower.contains(pName) || pName.split(" ").any { queryLower.contains(it) && it.length >= 3 }) {
                                            results.add(Pair(code, offProd))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (results.isNotEmpty()) break
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logWarn(TAG, "Search request failed for $searchUrl: ${e.message}")
            }
        }

        return results
    }

    // org.json gevşekti (tek tırnak, tırnaksız anahtar); aynı davranış için lenient parser.
    private fun parseJsonObject(body: String): JsonObject = Json { isLenient = true }.parseToJsonElement(body).jsonObject

    private fun parseOffResponse(json: JsonObject, fallbackCode: String): OffResponse? {
        val statusEl = json["status"]
        val statusInt = when (statusEl) {
            null, is JsonNull -> 0
            is JsonPrimitive -> if (statusEl.isString) {
                statusEl.content.toIntOrNull()
                    ?: if (statusEl.content.equals("success", ignoreCase = true)) 1 else 0
            } else {
                statusEl.contentOrNull?.toDoubleOrNull()?.toInt() ?: 0
            }
            else -> 0
        }
        val statusVerbose = json.optString("status_verbose", "")
        val code = json.optString("code", fallbackCode)
        val productJson = json.optObject("product")

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

    private fun parseOffProduct(json: JsonObject): OffProduct? {
        val name = json.optString("product_name").takeIf { it.isNotBlank() }
            ?: json.optString("product_name_en").takeIf { it.isNotBlank() }
            ?: json.optString("product_name_de").takeIf { it.isNotBlank() }
            ?: json.optString("product_name_fr").takeIf { it.isNotBlank() }
            ?: json.optString("generic_name").takeIf { it.isNotBlank() }

        // Reject only truly empty entries; keep products whose name is missing but that still
        // carry brand, ingredients, or an image (common for partially-filled OFF entries).
        val hasAnyUsableField = !name.isNullOrBlank() ||
            json.optString("ingredients_text").isNotBlank() ||
            json.optString("brands").isNotBlank() ||
            json.optString("image_front_url").isNotBlank() ||
            json.optString("image_url").isNotBlank()
        if (!hasAnyUsableField) {
            return null
        }

        fun extractStringList(key: String): List<String>? {
            val el = json[key] ?: return null
            return when {
                el is JsonArray -> {
                    val list = mutableListOf<String>()
                    for (item in el) {
                        val str = item.orgString()
                        if (str.isNotBlank()) list.add(str)
                    }
                    list.takeIf { it.isNotEmpty() }
                }
                el is JsonPrimitive && el.isString -> {
                    val str = el.content
                    if (str.isBlank()) null
                    else str.split(",").map { it.trim() }.filter { it.isNotEmpty() }
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
            val selected = json.optObject("selected_images")
            if (selected != null) {
                val front = selected.optObject("front") ?: selected.optObject("ingredients")
                if (front != null) {
                    val display = front.optObject("display") ?: front.optObject("small") ?: front.optObject("thumb")
                    if (display != null) {
                        val langs = listOf("de", "en", "fr", "tr", "ar", "it", "es", "nl", "pl", "ru")
                        for (lang in langs) {
                            val u = cleanUrl(display.optString(lang).takeIf { it.isNotBlank() })
                            if (!u.isNullOrBlank()) return u
                        }
                        for (k in display.keys) {
                            val u = cleanUrl(display.optString(k).takeIf { it.isNotBlank() })
                            if (!u.isNullOrBlank()) return u
                        }
                    }
                }
            }

            // 3. Inspect 'images' dictionary
            val imagesObj = json.optObject("images")
            if (imagesObj != null) {
                val frontKeys = listOf("front_tr", "front_en", "front_de", "front_fr", "front_ar", "front", "front_es", "1")
                for (key in frontKeys) {
                    val imgObj = imagesObj.optObject(key)
                    if (imgObj != null) {
                        val sizes = imgObj.optObject("sizes")
                        if (sizes != null) {
                            val size400 = sizes.optObject("400") ?: sizes.optObject("full") ?: sizes.optObject("display") ?: sizes.optObject("200")
                            val u = cleanUrl(size400?.optString("url")?.takeIf { it.isNotBlank() })
                            if (!u.isNullOrBlank()) return u
                        }
                    }
                }
            }

            // 4. Fallback to Open Food Facts CDN split barcode path only if 'images' has at least one key
            val rawCode = json.optString("code").takeIf { it.isNotBlank() }
            if (!rawCode.isNullOrBlank() && imagesObj != null && imagesObj.isNotEmpty()) {
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
            // optString turns a JSON null into the text "null", so read each key and fall back on a miss.
            nutriScore = parseNutriScore(json.optString("nutriscore_grade"))
                ?: parseNutriScore(json.optString("nutrition_grades")),
            novaGroup = parseNovaGroup(json.optString("nova_group", "")),
            imageUrl = resolvedImageUrl,
            imageFrontUrl = resolvedImageUrl,
            imageFrontSmallUrl = resolvedImageUrl,
            imageSmallUrl = resolvedImageUrl,
            imageThumbUrl = resolvedImageUrl,
            imageFrontThumbUrl = resolvedImageUrl
        )
    }

    companion object {
        // Only a plain A..E grade counts; OFF also sends "unknown", "not-applicable" or JSON null
        // (which optString turns into the text "null"), all of which mean "no score".
        fun parseNutriScore(raw: String): String? =
            raw.trim().uppercase().takeIf { it.length == 1 && it[0] in 'A'..'E' }

        // OFF sends nova_group as a JSON number (3); "4.0" also appears for some records, so read
        // it as a double and accept only whole numbers 1..4.
        fun parseNovaGroup(raw: String): Int? {
            val value = raw.trim().toDoubleOrNull() ?: return null
            return value.toInt().takeIf { value == floor(value) && it in 1..4 }
        }

        fun create(): OpenFoodFactsApi = OpenFoodFactsApi(createOffHttpClient())
    }
}
