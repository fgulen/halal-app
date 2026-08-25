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
        if (productDao.getProductCount() == 0) {
            productDao.insertProducts(InitialData.sampleProducts)
        }
    }

    suspend fun checkBarcode(barcode: String, language: AppLanguage = AppLanguage.EN): FoodProduct = withContext(Dispatchers.IO) {
        ensureDatabaseSeeded()
        val cleanedBarcode = barcode.trim()
        val localProduct = productDao.getProductByBarcode(cleanedBarcode)
        
        val product = if (localProduct != null) {
            localProduct.toDomainModel()
        } else {
            // Check if it matches any barcode in preloaded sample dataset
            val fallbackMatch = InitialData.sampleProducts.find { it.barcode == cleanedBarcode }
            if (fallbackMatch != null) {
                fallbackMatch.toDomainModel()
            } else {
                // Query Open Food Facts API (Global US & EU Food database)
                try {
                    val response = openFoodFactsApi.getProductByBarcode(cleanedBarcode)
                    if (response.status == 1 && response.product != null) {
                        val analyzedProduct = HalalAnalyzer.analyzeOpenFoodFactsProduct(
                            cleanedBarcode,
                            response.product,
                            language
                        )
                        // Cache in local DB
                        productDao.insertProduct(analyzedProduct.toEntity())
                        analyzedProduct
                    } else {
                        createNotFoundProduct(cleanedBarcode, language)
                    }
                } catch (e: Exception) {
                    createNotFoundProduct(cleanedBarcode, language)
                }
            }
        }

        // Save to scan history to track
        recordScan(product)
        product
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
