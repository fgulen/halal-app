package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus

@Entity(tableName = "scan_history")
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val barcode: String,
    val name: String,
    val brand: String,
    val category: String,
    val status: HalalStatus,
    val halalCertificate: String?,
    val harmfulOrSuspiciousIngredients: List<String>,
    val allIngredients: List<String>,
    val reasonOrDetails: String,
    val alternatives: List<String>,
    val imageUrl: String?,
    val scannedAt: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): FoodProduct {
        return FoodProduct(
            barcode = barcode,
            name = name,
            brand = brand,
            category = category,
            status = status,
            halalCertificate = halalCertificate,
            harmfulOrSuspiciousIngredients = harmfulOrSuspiciousIngredients,
            allIngredients = allIngredients,
            reasonOrDetails = reasonOrDetails,
            alternatives = alternatives,
            imageUrl = imageUrl,
            scannedAt = scannedAt
        )
    }

    companion object {
        fun fromDomain(product: FoodProduct): ScanHistoryEntity {
            return ScanHistoryEntity(
                barcode = product.barcode,
                name = product.name,
                brand = product.brand,
                category = product.category,
                status = product.status,
                halalCertificate = product.halalCertificate,
                harmfulOrSuspiciousIngredients = product.harmfulOrSuspiciousIngredients,
                allIngredients = product.allIngredients,
                reasonOrDetails = product.reasonOrDetails,
                alternatives = product.alternatives,
                imageUrl = product.imageUrl,
                scannedAt = System.currentTimeMillis()
            )
        }
    }
}
