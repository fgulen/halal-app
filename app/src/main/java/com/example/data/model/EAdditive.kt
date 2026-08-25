package com.example.data.model

data class EAdditive(
    val code: String,
    val name: String,
    val status: HalalStatus,
    val origin: String, // Bitkisel, Hayvansal (Domuz/Sığır), Sentetik, Böcek
    val description: String,
    val commonUsage: String
)
