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
    val scannedAt: Long = com.example.data.util.nowEpochMillis(),
    // Which language this analysis (reasonOrDetails, flaggedDetails reasons, certificate text)
    // was rendered in. Lets the repository detect a stale-language cache entry and re-analyze
    // instead of silently returning text in a different language than the current UI.
    val language: AppLanguage = AppLanguage.EN,
    // Which madhhab produced this verdict. Stored with cache and history rows so a result is
    // always re-displayed (e.g. ingredient chip colours) under the madhhab it was analyzed with,
    // not whatever the user has selected now. Fixed-verdict curated entries stay UNSPECIFIED.
    val madhhab: Madhhab = Madhhab.UNSPECIFIED,
    // Health indicators from Open Food Facts (display only, never part of the halal verdict).
    // nutriScore is "A".."E", novaGroup is 1..4; null when OFF has no value for the product.
    val nutriScore: String? = null,
    val novaGroup: Int? = null
)
