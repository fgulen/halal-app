package com.example.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

// Testler için OFF istemcisi. world.openfoodfacts.org'a verilen cevabı döner; diğer hostlar 404 alır.
// failOffFood açıkken OFF isteği ağ hatasıyla düşer.
fun offMockClient(
    offFoodStatus: Int = 200,
    offFoodBody: String = "{}",
    onOffFoodRequest: () -> Unit = {},
    failOffFood: Boolean = false
): HttpClient = HttpClient(MockEngine { request ->
    val isOffFood = request.url.host == "world.openfoodfacts.org"
    if (isOffFood) onOffFoodRequest()
    if (isOffFood && failOffFood) throw java.io.IOException("offline")
    respond(
        content = if (isOffFood) offFoodBody else "{}",
        status = HttpStatusCode.fromValue(if (isOffFood) offFoodStatus else 404),
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}) {
    expectSuccess = false
}
