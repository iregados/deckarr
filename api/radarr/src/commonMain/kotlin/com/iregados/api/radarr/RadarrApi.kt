package com.iregados.api.radarr

import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.dto.RadarrApiConfig
import com.iregados.api.radarr.dto.RadarrImage
import com.iregados.api.radarr.serializers.ImageUrlSerializer
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
import kotlinx.serialization.modules.SerializersModule

class RadarrApi(
    private var protocol: URLProtocol,
    private var host: String,
    private var apiKey: String
) {
    private val log = Logger(
        config = loggerConfigInit(
            platformLogWriter(NoTagFormatter)
        ),
        tag = "RadarrApi"
    )
    private var client = createClient(protocol, host, apiKey)
    private val _configFlow = MutableStateFlow(RadarrApiConfig(protocol, host, apiKey))
    val configFlow: StateFlow<RadarrApiConfig> = _configFlow.asStateFlow()

    fun configure(
        protocol: URLProtocol,
        host: String,
        apiKey: String
    ) {
        this.protocol = protocol
        this.host = host
        this.apiKey = apiKey
        client = createClient(protocol, host, apiKey)
        _configFlow.value = RadarrApiConfig(protocol, host, apiKey)
    }

    private fun createClient(
        protocol: URLProtocol,
        host: String,
        apiKey: String
    ): HttpClient {
        return HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000 // 30 seconds
                connectTimeoutMillis = 10_000 // 10 seconds
                socketTimeoutMillis = 30_000 // 30 seconds
            }

            install(ContentNegotiation) {
                val radarrImageModule = SerializersModule {
                    contextual(
                        RadarrImage::class,
                        ImageUrlSerializer("${protocol.name}://${host.substringBefore("/")}")
                    )
                }

                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                        serializersModule = radarrImageModule
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
            log.e("apiGet $path error: ${e.message ?: "Unknown error"}")
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