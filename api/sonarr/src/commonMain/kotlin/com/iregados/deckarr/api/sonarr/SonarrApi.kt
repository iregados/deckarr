package com.iregados.deckarr.api.sonarr

import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.util.HttpClientFactory
import com.iregados.deckarr.api.common.util.toApiError
import com.iregados.deckarr.api.sonarr.dto.SonarrApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.URLProtocol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlin.coroutines.cancellation.CancellationException

class SonarrApi(
    private var protocol: URLProtocol,
    private var host: String,
    private var apiKey: String,
    private var basePath: String
) : AutoCloseable {
    private val log = Logger(
        config = loggerConfigInit(
            platformLogWriter(NoTagFormatter)
        ),
        tag = "com.iregados.deckarr.api.common.SonarrApi"
    )
    private var client = createClient(protocol, host, apiKey)
    private val _configFlow =
        MutableStateFlow<SonarrApiConfig?>(null)
    val configFlow: Flow<SonarrApiConfig> =
        _configFlow.filterNotNull()

    override fun close() {
        client.close()
    }

    fun configure(
        protocol: URLProtocol,
        host: String,
        apiKey: String,
        basePath: String
    ) {
        this.protocol = protocol
        this.host = host
        this.apiKey = apiKey
        this.basePath = basePath

        val oldClient = client
        client = createClient(protocol, host, apiKey)
        oldClient.close()

        _configFlow.value = SonarrApiConfig(
            protocol,
            host,
            apiKey
        )
    }

    private fun createClient(
        protocol: URLProtocol,
        host: String,
        apiKey: String,
    ): HttpClient {
        return HttpClientFactory.create(
            requestTimeoutMillis = 60_000,
            connectTimeoutMillis = 30_000,
            socketTimeoutMillis = 60_000
        ) {
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
            val response = client.get("$basePath$path")
            return ApiResult.Success(response.body())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            log.e("apiGet error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.toApiError())
        }
    }

    internal suspend inline fun apiDelete(path: String): ApiResult<Boolean> {
        try {
            client.delete("$basePath$path")
            return ApiResult.Success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            log.e("apiDelete error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.toApiError())
        }
    }

    internal suspend inline fun <reified Req, reified Res> apiPost(
        path: String,
        body: Req
    ): ApiResult<Res> {
        try {
            val response = client.post("$basePath$path") {
                header("Content-Type", "application/json")
                setBody(body)
            }
            return ApiResult.Success(response.body())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            log.e("apiPost error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.toApiError())
        }
    }
}