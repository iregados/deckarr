package com.iregados.deckarr.api.common.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(
        json: Json = defaultJson,
        expectSuccess: Boolean = true,
        requestTimeoutMillis: Long = 30_000,
        connectTimeoutMillis: Long = 10_000,
        socketTimeoutMillis: Long = 30_000,
        configure: HttpClientConfig<*>.() -> Unit = {}
    ): HttpClient = HttpClient {
        this.expectSuccess = expectSuccess

        install(HttpTimeout) {
            this.requestTimeoutMillis = requestTimeoutMillis
            this.connectTimeoutMillis = connectTimeoutMillis
            this.socketTimeoutMillis = socketTimeoutMillis
        }

        install(ContentNegotiation) {
            json(json)
        }

        configure()
    }
}

private val defaultJson = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = false
}