package com.example.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout

// Platform motoru: Android/JVM'de OkHttp, iOS'ta Darwin (NSURLSession).
expect fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

// Eski OkHttp ayarlarıyla aynı: bağlantı ve okuma zaman aşımı 6 sn, yönlendirmeler izlenir.
// expectSuccess kapalı: 4xx/5xx yanıtlar exception değil, normal yanıt olarak döner (OFF 502/404 ayrımı için).
fun createOffHttpClient(): HttpClient = platformHttpClient {
    install(HttpTimeout) {
        connectTimeoutMillis = 6_000
        socketTimeoutMillis = 6_000
    }
    expectSuccess = false
}
