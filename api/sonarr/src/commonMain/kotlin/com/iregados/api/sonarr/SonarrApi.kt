package com.iregados.api.sonarr

import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.dto.SonarrApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class SonarrApi(
    private var protocol: URLProtocol,
    private var host: String,
    private var apiKey: String
) {
    private val log = Logger(
        config = loggerConfigInit(
            platformLogWriter(NoTagFormatter)
        ),
        tag = "SonarrApi"
    )
    private var client = createClient(protocol, host, apiKey)
    private val _configFlow = MutableStateFlow(SonarrApiConfig(protocol, host, apiKey))
    val configFlow: StateFlow<SonarrApiConfig> = _configFlow.asStateFlow()

    fun configure(
        protocol: URLProtocol,
        host: String,
        apiKey: String
    ) {
        this.protocol = protocol
        this.host = host
        this.apiKey = apiKey
        client = createClient(protocol, host, apiKey)
        _configFlow.value = SonarrApiConfig(protocol, host, apiKey)
    }

    private fun createClient(
        protocol: URLProtocol,
        host: String,
        apiKey: String
    ): HttpClient {
        return HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 60_000
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    }
                )
            }

            defaultRequest {
                url {
                    this.protocol = protocol
                    this.host = host
                }
                header("Accept", "application/json")
                header("X-Api-Key", apiKey)
            }
        }
    }

    internal suspend inline fun <reified T> apiGet(path: String): ApiResult<T> {
        try {
            val response = client.get(path)
            return ApiResult.Success(response.body())
        } catch (e: Exception) {
            log.e("apiGet error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    internal suspend inline fun apiDelete(path: String): ApiResult<Boolean> {
        try {
            client.delete(path)
            return ApiResult.Success(true)
        } catch (e: Exception) {
            log.e("apiDelete error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    internal suspend inline fun <reified Req, reified Res> apiPost(
        path: String,
        body: Req
    ): ApiResult<Res> {
        try {
            val response = client.post(path) {
                header("Content-Type", "application/json")
                setBody(body)
            }
            return ApiResult.Success(response.body())
        } catch (e: Exception) {
            log.e("apiPost error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.message ?: "Unknown error")
        }
    }
}