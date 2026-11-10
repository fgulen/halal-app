package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.AppLanguage
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val barcode: String,
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
    // Language this cached analysis was rendered in. Curated InitialData.kt entries are always
    // English regardless of this value (they're plain hardcoded text, not per-language) - it only
    // drives re-analysis for rows written by the live OFF analyzer, see ProductRepository.
    val language: AppLanguage = AppLanguage.EN,
    // Madhhab this cached verdict was computed under (v5). A hit under a different madhhab is
    // stale and re-analyzed, the same way a different-language hit is.
    val madhhab: Madhhab = Madhhab.UNSPECIFIED,
    // Open Food Facts health indicators (v7). Null on rows cached before v7 (those are cleared).
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
            language = language,
            madhhab = madhhab,
            nutriScore = nutriScore,
            novaGroup = novaGroup
        )
    }

    companion object {
        fun fromDomain(product: FoodProduct): ProductEntity {
            return ProductEntity(
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
                language = product.language,
                madhhab = product.madhhab,
                nutriScore = product.nutriScore,
                novaGroup = product.novaGroup
            )
        }
    }
}

fun FoodProduct.toEntity(): ProductEntity = ProductEntity.fromDomain(this)

