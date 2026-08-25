package com.example.data.repository

import com.example.data.analyzer.HalalAnalyzer
import com.example.data.local.InitialData
import com.example.data.local.ProductDao
import com.example.data.local.ProductEntity
import com.example.data.local.ScanHistoryEntity
import com.example.data.local.toEntity
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

    suspend fun checkBarcode(barcode: String): FoodProduct = withContext(Dispatchers.IO) {
        ensureDatabaseSeeded()
        val cleanedBarcode = barcode.trim()
        val localProduct = productDao.getProductByBarcode(cleanedBarcode)
        
        val product = if (localProduct != null) {
            localProduct.toDomainModel()
        } else {
            // Check if it matches any barcode in preloaded data directly
            val fallbackMatch = InitialData.sampleProducts.find { it.barcode == cleanedBarcode }
            if (fallbackMatch != null) {
                fallbackMatch.toDomainModel()
            } else {
                // Query Open Food Facts API (The primary European/Global food database)
                try {
                    val response = openFoodFactsApi.getProductByBarcode(cleanedBarcode)
                    if (response.status == 1 && response.product != null) {
                        val analyzedProduct = HalalAnalyzer.analyzeOpenFoodFactsProduct(
                            cleanedBarcode,
                            response.product
                        )
                        // Save in local DB for fast future scans
                        productDao.insertProduct(analyzedProduct.toEntity())
                        analyzedProduct
                    } else {
                        createNotFoundProduct(cleanedBarcode)
                    }
                } catch (e: Exception) {
                    createNotFoundProduct(cleanedBarcode)
                }
            }
        }

        // Save to scan history to track
        recordScan(product)
        product
    }

    private fun createNotFoundProduct(barcode: String): FoodProduct {
        return FoodProduct(
            barcode = barcode,
            name = "Kayıtsız Ürün",
            brand = "Bilinmiyor",
            category = "Genel Gıda",
            status = HalalStatus.BULUNAMADI,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = emptyList(),
            reasonOrDetails = "Bu barkod ($barcode) yerel ve Avrupa veri tabanında (Open Food Facts) henüz bulunamadı. Lütfen paket üzerindeki içindekiler kısmını E-Kodları Rehberimiz ile karşılaştırın.",
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
