package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.data.util.nowEpochMillis

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
    val scannedAt: Long = nowEpochMillis(),
    // Madhhab the verdict was computed under (v5), so history re-renders consistently.
    val madhhab: Madhhab = Madhhab.UNSPECIFIED,
    // Health indicators (v7). Old history rows stay null and show no gauge.
    val nutriScore: String? = null,
    val novaGroup: Int? = null
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
            scannedAt = scannedAt,
            madhhab = madhhab,
            nutriScore = nutriScore,
            novaGroup = novaGroup
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
                scannedAt = nowEpochMillis(),
                madhhab = product.madhhab,
                nutriScore = product.nutriScore,
                novaGroup = product.novaGroup
            )
        }
    }
}
