package com.example.data.model

object ReportEmail {

    const val SUPPORT_EMAIL = "fatihgulen@gmail.com"

    fun buildSubject(product: FoodProduct, language: AppLanguage): String =
        "${AppStrings.getReportErrorEmailSubject(language)}: ${product.name}"

    fun buildBody(product: FoodProduct, language: AppLanguage): String = buildString {
        append("${AppStrings.getShareProductLabel(language)}: ${product.name} (${product.brand})\n")
        append("${AppStrings.getShareBarcodeLabel(language)}: ${product.barcode}\n")
        append("${AppStrings.getShareStatusLabel(language)}: ${AppStrings.getStatusLabel(product.status, language)}\n")
        if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) {
            append(
                "${AppStrings.getShareFlaggedIngredientsLabel(language)}: " +
                    "${product.harmfulOrSuspiciousIngredients.joinToString(", ")}\n"
            )
        }
        append("\n${AppStrings.getReportErrorPromptLine(language)}\n")
    }
}
