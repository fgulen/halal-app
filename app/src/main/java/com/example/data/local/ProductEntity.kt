package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus

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
    val imageUrl: String?
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
            imageUrl = imageUrl
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
                imageUrl = product.imageUrl
            )
        }
    }
}

fun FoodProduct.toEntity(): ProductEntity = ProductEntity.fromDomain(this)

