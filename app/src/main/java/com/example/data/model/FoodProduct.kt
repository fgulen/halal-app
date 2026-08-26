package com.example.data.model

data class FlaggedIngredient(
    val name: String,
    val eCode: String? = null,
    val status: HalalStatus,
    val reason: String,
    val origin: String? = null
)

data class FoodProduct(
    val barcode: String,
    val name: String,
    val brand: String,
    val category: String,
    val status: HalalStatus,
    val halalCertificate: String? = null,
    val harmfulOrSuspiciousIngredients: List<String> = emptyList(),
    val flaggedDetails: List<FlaggedIngredient> = emptyList(),
    val allIngredients: List<String> = emptyList(),
    val reasonOrDetails: String = "",
    val alternatives: List<String> = emptyList(),
    val imageUrl: String? = null,
    val scannedAt: Long = System.currentTimeMillis()
)
