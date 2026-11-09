package com.example.data.model

data class EAdditive(
    val code: String,
    val name: LocalizedText,
    val status: HalalStatus,
    val origin: LocalizedText, // Bitkisel, Hayvansal (Domuz/Sığır), Sentetik, Böcek, Fermantasyon
    val description: LocalizedText,
    val alternateNames: List<String> = emptyList(),
    val commonUsage: LocalizedText
)
