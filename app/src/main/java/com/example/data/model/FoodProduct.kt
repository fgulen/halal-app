package com.example.data.model

data class FoodProduct(
    val barcode: String,
    val name: String,
    val brand: String,
    val category: String,
    val status: HalalStatus,
    val halalCertificate: String? = null,
    val harmfulOrSuspiciousIngredients: List<String> = emptyList(),
    val allIngredients: List<String> = emptyList(),
    val reasonOrDetails: String = "",
    val alternatives: List<String> = emptyList(),
    val imageUrl: String? = null,
    val scannedAt: Long = System.currentTimeMillis()
)
