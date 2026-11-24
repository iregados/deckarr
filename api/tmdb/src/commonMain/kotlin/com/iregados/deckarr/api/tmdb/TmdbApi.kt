package com.iregados.deckarr.api.tmdb

import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.util.HttpClientFactory
import com.iregados.deckarr.api.common.util.toApiError
import com.iregados.deckarr.api.tmdb.dto.TmdbApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import kotlin.coroutines.cancellation.CancellationException

class TmdbApi(
    private val config: TmdbApiConfig
) : AutoCloseable {
    private val log = Logger(
        config = loggerConfigInit(
            platformLogWriter(NoTagFormatter)
        ),
        tag = "com.iregados.deckarr.api.tmdb.TmdbApi"
    )
    private val client: HttpClient = createClient(config.accessToken)

    override fun close() {
        client.close()
    }

    private fun createClient(accessToken: String): HttpClient {
        return HttpClientFactory.create {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HOST
                }
                header(HttpHeaders.Accept, "application/json")
                if (accessToken.isNotBlank()) {
                    header(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }
        }
    }

    internal suspend inline fun <reified T> apiGet(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): ApiResult<T> {
        try {
            val response = client.get("$BASE_PATH$path", block)
            return ApiResult.Success(response.body())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            log.e("apiGet error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.toApiError())
        }
    }

    companion object {
        private const val HOST = "api.themoviedb.org"
        private const val BASE_PATH = "/3"
    }
}