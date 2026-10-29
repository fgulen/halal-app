package com.example.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.data.analyzer.HalalAnalyzer
import com.example.data.local.InitialData
import com.example.data.local.ProductDao
import com.example.data.local.ProductEntity
import com.example.data.local.ScanHistoryEntity
import com.example.data.local.toEntity
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.data.remote.OpenFoodFactsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val TAG = "ProductRepository"

class ProductRepository(
    private val productDao: ProductDao,
    private val openFoodFactsApi: OpenFoodFactsApi = OpenFoodFactsApi.create(),
    private val appContext: Context? = null
) {

    /**
     * Thrown when the product genuinely could not be checked because of a connectivity
     * problem, as opposed to a confirmed "not in Open Food Facts" result. Callers must not
     * treat this as a not-found verdict: no history entry should be recorded for it.
     */
    class NetworkUnavailableException(val query: String) : Exception("No network reachable while checking '$query'")

    private fun isOnline(): Boolean {
        val context = appContext ?: return true
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return true
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    // The Nutella shortcut and curated InitialData samples below return fixed verdicts that don't
    // depend on the madhhab, so they keep their own UNSPECIFIED stamp rather than the requested one.
    suspend fun checkBarcode(
        input: String,
        language: AppLanguage = AppLanguage.EN,
        madhhab: Madhhab = Madhhab.UNSPECIFIED
    ): FoodProduct = withContext(Dispatchers.IO) {
        val query = input.trim()
        if (query.isBlank()) {
            return@withContext createNotFoundProduct(query, language)
        }

        // Known offline up front: skip the live OFF calls below entirely (no point eating
        // two request timeouts) but still let the Room cache and curated fallback answer -
        // both work without a network.
        var hadNetworkError = !isOnline()
        var staleLanguageCacheHit: ProductEntity? = null
        // Same language and madhhab, but cached before the health columns existed (no Nutri-Score or
        // NOVA). Used only if live OFF has nothing; otherwise the live answer replaces it.
        var cachedWithoutHealth: ProductEntity? = null
        val lowerQuery = query.lowercase()

        // 1. Direct Nutella / Ferrero intelligent matcher
        if (lowerQuery == "nutella" || lowerQuery.contains("nutella") || lowerQuery.contains("nutela")) {
            val nutellaSample = InitialData.sampleProducts.find {
                it.barcode == "4008400404127" || it.barcode == "3017620422003" || it.name.lowercase().contains("nutella")
            }
            if (nutellaSample != null) {
                val domain = nutellaSample.toDomainModel()
                productDao.insertProduct(nutellaSample)
                recordScan(domain)
                return@withContext domain
            }
        }

        // 2. Detect if user pasted ingredients text directly (e.g. contains commas, E-numbers, or ingredient keywords)
        val hasIngredientIndicators = query.contains(",") && (
            lowerQuery.contains("e-") || lowerQuery.contains("e1") || lowerQuery.contains("e4") ||
            lowerQuery.contains("gelatin") || lowerQuery.contains("sugar") || lowerQuery.contains("şeker") ||
            lowerQuery.contains("oil") || lowerQuery.contains("yağ") || lowerQuery.contains("flour") ||
            lowerQuery.contains("un") || lowerQuery.contains("su") || lowerQuery.contains("lecithin") ||
            lowerQuery.contains("emulsifier") || lowerQuery.contains("aroma")
        )
        if (hasIngredientIndicators && query.length > 25) {
            val analyzed = HalalAnalyzer.analyzeIngredientsText(
                productName = "Custom Ingredient List",
                ingredientsText = query,
                barcode = "CUSTOM",
                language = language,
                madhhab = madhhab
            )
            recordScan(analyzed)
            return@withContext analyzed
        }

        val digitsOnly = query.filter { it.isDigit() }

        // 3. Barcode search: Room cache, then live Open Food Facts, then curated fallback.
        // Live OFF is checked BEFORE the curated list (not after): a scanned barcode is a
        // real-world claim about a real product, and a curated entry must never be allowed
        // to preempt the actual product that barcode belongs to. This ordering is the fix for
        // a class of bugs where a curated entry's barcode was wrong/typo'd but happened to
        // collide with a real, different product's real barcode (e.g. a hardcoded "Milka
        // Daim" entry that was actually LU's real barcode for "Prince" biscuits) - previously
        // curated data was checked first and permanently hijacked every real scan of that
        // barcode. Curated data now only answers once OFF has confirmed it has nothing.
        if (digitsOnly.isNotEmpty()) {
            val candidates = buildList {
                add(digitsOnly)
                if (digitsOnly.length == 12) add("0$digitsOnly")
                if (digitsOnly.length == 13 && digitsOnly.startsWith("0")) add(digitsOnly.substring(1))
                if (query != digitsOnly && query.isNotBlank()) add(query)
            }.distinct()

            for (barcodeCandidate in candidates) {
                // Check local Room cached DB. A cache hit in a *different* language or madhhab
                // than requested is stale, not a valid result - fall through to the live OFF
                // fetch below instead of returning it, so re-scanning after a language or madhhab
                // switch re-analyzes instead of returning text/verdict frozen under whatever
                // settings the product was first analyzed with.
                val localProduct = productDao.getProductByBarcode(barcodeCandidate)
                if (localProduct != null && localProduct.status != HalalStatus.BULUNAMADI) {
                    val hasHealthData = localProduct.nutriScore != null || localProduct.novaGroup != null
                    if (localProduct.language == language && localProduct.madhhab == madhhab) {
                        // Offline, or the cache already has health data: serve it. Otherwise refetch
                        // so pre-v7 rows get their Nutri-Score/NOVA.
                        if (hasHealthData || hadNetworkError) {
                            val domain = localProduct.toDomainModel()
                            recordScan(domain)
                            return@withContext domain
                        }
                        if (cachedWithoutHealth == null) cachedWithoutHealth = localProduct
                    } else if (staleLanguageCacheHit == null) {
                        // Cached in a different language than requested - not a valid result on
                        // its own (see ordering note below), but a real answer worth keeping in
                        // reserve in case OFF turns out to be unreachable.
                        staleLanguageCacheHit = localProduct
                    }
                }
            }

            // 4. Query live global Open Food Facts API (multi-region v2/v0 endpoints)
            if (!hadNetworkError) {
                try {
                    val response = openFoodFactsApi.getProductByBarcode(digitsOnly)
                    if ((response.status == 1 || response.product != null) && response.product != null) {
                        val analyzedProduct = HalalAnalyzer.analyzeOpenFoodFactsProduct(
                            response.code ?: digitsOnly,
                            response.product,
                            language,
                            madhhab
                        )
                        Log.d(TAG, "Resolved barcode=$digitsOnly name=${analyzedProduct.name} image=${analyzedProduct.imageUrl}")
                        productDao.insertProduct(analyzedProduct.toEntity())
                        recordScan(analyzedProduct)
                        return@withContext analyzedProduct
                    }
                    // getProductByBarcode swallows its own request exceptions and reports the
                    // outcome via statusVerbose instead of throwing, so check it here rather
                    // than relying on the catch block below (which network failures never reach).
                    if (response.statusVerbose == "network error") {
                        hadNetworkError = true
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Open Food Facts lookup failed for barcode=$digitsOnly: ${e.message}")
                    hadNetworkError = true
                }
            }

            // 5. Curated fallback - only reached once live OFF confirmed it has nothing (or
            // is unreachable) for every candidate barcode, so it can no longer shadow real
            // product data.
            for (barcodeCandidate in candidates) {
                val sampleMatch = InitialData.sampleProducts.find { it.barcode == barcodeCandidate }
                if (sampleMatch != null) {
                    val domain = sampleMatch.toDomainModel()
                    productDao.insertProduct(sampleMatch)
                    recordScan(domain)
                    return@withContext domain
                }
            }
        }

        // 6. Name / Keyword search in local curated database
        val sampleNameMatch = InitialData.sampleProducts.find {
            it.name.lowercase().contains(lowerQuery) ||
            it.brand.lowercase().contains(lowerQuery) ||
            it.barcode.contains(lowerQuery)
        }
        if (sampleNameMatch != null) {
            val domain = sampleNameMatch.toDomainModel()
            productDao.insertProduct(sampleNameMatch)
            recordScan(domain)
            return@withContext domain
        }

        // 7. Search live Open Food Facts by product name / terms
        if (!hadNetworkError) {
            try {
                val searchResults = openFoodFactsApi.searchProductsByName(query)
                if (searchResults.isNotEmpty()) {
                    val (code, offProduct) = searchResults.first()
                    val analyzedProduct = HalalAnalyzer.analyzeOpenFoodFactsProduct(
                        code,
                        offProduct,
                        language,
                        madhhab
                    )
                    productDao.insertProduct(analyzedProduct.toEntity())
                    recordScan(analyzedProduct)
                    return@withContext analyzedProduct
                }
            } catch (e: Exception) {
                Log.w(TAG, "Open Food Facts name search failed for '$query': ${e.message}")
                hadNetworkError = true
            }
        }

        // A network failure is not a verdict about the product: fall back to a cache hit in
        // the wrong language rather than declaring it unavailable, and never let a connectivity
        // problem masquerade as a confirmed "not found" - surface it to the caller distinctly so
        // it isn't recorded into scan history as one.
        if (hadNetworkError) {
            staleLanguageCacheHit?.let { cached ->
                val domain = cached.toDomainModel()
                recordScan(domain)
                return@withContext domain
            }
            throw NetworkUnavailableException(if (digitsOnly.isNotEmpty()) digitsOnly else query)
        }

        // Live OFF had nothing new, but this product is cached: keep the cached answer.
        cachedWithoutHealth?.let { cached ->
            val domain = cached.toDomainModel()
            recordScan(domain)
            return@withContext domain
        }

        val notFound = createNotFoundProduct(if (digitsOnly.isNotEmpty()) digitsOnly else query, language)
        recordScan(notFound)
        notFound
    }

    private fun createNotFoundProduct(barcode: String, language: AppLanguage): FoodProduct {
        val name = when (language) {
            AppLanguage.EN -> "Unregistered Product"
            AppLanguage.DE -> "Nicht registriertes Produkt"
            AppLanguage.FR -> "Produit non répertorié"
            AppLanguage.TR -> "Kayıtsız Ürün"
            AppLanguage.AR -> "منتج غير مسجل"
        }
        val brand = when (language) {
            AppLanguage.EN -> "Unknown Brand"
            AppLanguage.DE -> "Unbekannte Marke"
            AppLanguage.FR -> "Marque inconnue"
            AppLanguage.TR -> "Bilinmeyen Marka"
            AppLanguage.AR -> "علامة غير معروفة"
        }
        val reason = when (language) {
            AppLanguage.EN -> "Barcode ($barcode) was not found in Open Food Facts (EU/US). Please check ingredients list on the packaging for E-additives."
            AppLanguage.DE -> "Barcode ($barcode) wurde in Open Food Facts nicht gefunden. Bitte Zutatenliste auf der Packung prüfen."
            AppLanguage.FR -> "Code-barres ($barcode) non trouvé dans Open Food Facts. Veuillez inspecter la liste des ingrédients sur l'emballage."
            AppLanguage.TR -> "Bu barkod ($barcode) küresel veri tabanında bulunamadı. Lütfen paket üzerindeki içindekiler kısmını inceleyin."
            AppLanguage.AR -> "لم يتم العثور على هذا الباركود ($barcode) في قاعدة البيانات. يرجى مراجعة المكونات على الغلاف."
        }
        return FoodProduct(
            barcode = barcode,
            name = name,
            brand = brand,
            category = "Global Food",
            status = HalalStatus.BULUNAMADI,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = emptyList(),
            reasonOrDetails = reason,
            alternatives = emptyList(),
            imageUrl = null,
            language = language
        )
    }

    suspend fun recordScan(product: FoodProduct) = withContext(Dispatchers.IO) {
        productDao.insertScanHistory(ScanHistoryEntity.fromDomain(product))
    }

    /**
     * Analyzes ingredients text captured from a photo (OCR) for a product that has no barcode
     * match - e.g. reached from the "not found" screen. Runs entirely offline (no OFF lookup)
     * and records the result to scan history like any other scan, keyed by a synthetic barcode
     * since there is no real one to key it by.
     */
    suspend fun analyzeIngredientsPhoto(
        productName: String,
        ingredientsText: String,
        language: AppLanguage,
        madhhab: Madhhab = Madhhab.UNSPECIFIED
    ): FoodProduct = withContext(Dispatchers.IO) {
        val barcode = "PHOTO-${System.currentTimeMillis()}"
        val analyzed = HalalAnalyzer.analyzeIngredientsText(
            productName = productName.ifBlank { AppStrings.getPhotoAnalysisDefaultName(language) },
            ingredientsText = ingredientsText,
            barcode = barcode,
            language = language,
            madhhab = madhhab
        )
        recordScan(analyzed)
        analyzed
    }

    fun getScanHistory(): Flow<List<FoodProduct>> {
        return productDao.getScanHistory().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    suspend fun deleteScanHistory(id: Long) = withContext(Dispatchers.IO) {
        productDao.deleteScanHistory(id)
    }

    suspend fun clearScanHistory() = withContext(Dispatchers.IO) {
        productDao.clearScanHistory()
    }

    fun searchProducts(query: String): Flow<List<FoodProduct>> {
        return productDao.searchProducts(query).map { list ->
            list.map { it.toDomainModel() }
        }
    }

}
