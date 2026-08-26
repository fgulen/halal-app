package com.example.data.repository

import com.example.data.analyzer.HalalAnalyzer
import com.example.data.local.InitialData
import com.example.data.local.ProductDao
import com.example.data.local.ProductEntity
import com.example.data.local.ScanHistoryEntity
import com.example.data.local.toEntity
import com.example.data.model.AppLanguage
import com.example.data.model.EAdditive
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.remote.OpenFoodFactsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productDao: ProductDao,
    private val openFoodFactsApi: OpenFoodFactsApi = OpenFoodFactsApi.create()
) {

    suspend fun ensureDatabaseSeeded() = withContext(Dispatchers.IO) {
        // Seed or refresh sample products to ensure images and data are up-to-date
        productDao.insertProducts(InitialData.sampleProducts)
    }

    suspend fun checkBarcode(input: String, language: AppLanguage = AppLanguage.EN): FoodProduct = withContext(Dispatchers.IO) {
        ensureDatabaseSeeded()
        val query = input.trim()
        if (query.isBlank()) {
            return@withContext createNotFoundProduct(query, language)
        }

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
                language = language
            )
            recordScan(analyzed)
            return@withContext analyzed
        }

        val digitsOnly = query.filter { it.isDigit() }

        // 3. Barcode search across local curated, database, and live Open Food Facts
        if (digitsOnly.isNotEmpty()) {
            val candidates = buildList {
                add(digitsOnly)
                if (digitsOnly.length == 12) add("0$digitsOnly")
                if (digitsOnly.length == 13 && digitsOnly.startsWith("0")) add(digitsOnly.substring(1))
                if (query != digitsOnly && query.isNotBlank()) add(query)
            }.distinct()

            for (barcodeCandidate in candidates) {
                // Check sample products first (curated verified list)
                val sampleMatch = InitialData.sampleProducts.find { it.barcode == barcodeCandidate }
                if (sampleMatch != null) {
                    val domain = sampleMatch.toDomainModel()
                    productDao.insertProduct(sampleMatch)
                    recordScan(domain)
                    return@withContext domain
                }

                // Check local Room cached DB
                val localProduct = productDao.getProductByBarcode(barcodeCandidate)
                if (localProduct != null && localProduct.status != HalalStatus.BULUNAMADI) {
                    val domain = localProduct.toDomainModel()
                    recordScan(domain)
                    return@withContext domain
                }
            }

            // 4. Query live global Open Food Facts API (multi-region v2/v0 endpoints)
            try {
                val response = openFoodFactsApi.getProductByBarcode(digitsOnly)
                if ((response.status == 1 || response.product != null) && response.product != null) {
                    val analyzedProduct = HalalAnalyzer.analyzeOpenFoodFactsProduct(
                        response.code ?: digitsOnly,
                        response.product,
                        language
                    )
                    productDao.insertProduct(analyzedProduct.toEntity())
                    recordScan(analyzedProduct)
                    return@withContext analyzedProduct
                }
            } catch (_: Exception) {
                // fall through
            }
        }

        // 5. Name / Keyword search in local curated database
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

        // 6. Search live Open Food Facts by product name / terms
        try {
            val searchResults = openFoodFactsApi.searchProductsByName(query)
            if (searchResults.isNotEmpty()) {
                val (code, offProduct) = searchResults.first()
                val analyzedProduct = HalalAnalyzer.analyzeOpenFoodFactsProduct(
                    code,
                    offProduct,
                    language
                )
                productDao.insertProduct(analyzedProduct.toEntity())
                recordScan(analyzedProduct)
                return@withContext analyzedProduct
            }
        } catch (_: Exception) {
            // fall through
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
            imageUrl = null
        )
    }

    suspend fun recordScan(product: FoodProduct) = withContext(Dispatchers.IO) {
        productDao.insertScanHistory(ScanHistoryEntity.fromDomain(product))
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

    fun getEAdditives(searchQuery: String = "", filterStatus: HalalStatus? = null): List<EAdditive> {
        val q = searchQuery.trim().lowercase()
        return InitialData.eAdditivesDirectory.filter { item ->
            val matchesQuery = q.isEmpty() ||
                    item.code.lowercase().contains(q) ||
                    item.name.lowercase().contains(q) ||
                    item.description.lowercase().contains(q) ||
                    item.commonUsage.lowercase().contains(q)
            val matchesStatus = filterStatus == null || item.status == filterStatus
            matchesQuery && matchesStatus
        }
    }
}
