package com.example.data.remote


data class OffProduct(
    val productName: String? = null,
    val productNameEn: String? = null,
    val productNameDe: String? = null,
    val productNameFr: String? = null,
    val productNameEs: String? = null,
    val productNameTr: String? = null,
    val productNameAr: String? = null,
    val brands: String? = null,
    val categories: String? = null,
    val countriesTags: List<String>? = null,
    val labelsTags: List<String>? = null,
    val ingredientsText: String? = null,
    val ingredientsTextEn: String? = null,
    val ingredientsTextDe: String? = null,
    val ingredientsTextFr: String? = null,
    val ingredientsTextEs: String? = null,
    val ingredientsTextTr: String? = null,
    val ingredientsTextAr: String? = null,
    val additivesTags: List<String>? = null,
    val ingredientsAnalysisTags: List<String>? = null,
    val imageUrl: String? = null,
    val imageFrontUrl: String? = null,
    val imageFrontSmallUrl: String? = null,
    val imageSmallUrl: String? = null,
    val imageThumbUrl: String? = null,
    val imageFrontThumbUrl: String? = null,
    val nutriScore: String? = null,
    val novaGroup: Int? = null
)
