package com.iregados.deckarr.api.qbittorrent

import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.iregados.deckarr.api.common.ApiError
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.interfaces.TorrentClientApi
import com.iregados.deckarr.api.common.util.HttpClientFactory
import com.iregados.deckarr.api.common.util.toApiError
import com.iregados.deckarr.api.qbittorrent.dto.QBitTorrentApiConfig
import com.iregados.deckarr.api.qbittorrent.methods.internalGetTorrentListAndStats
import com.iregados.deckarr.api.qbittorrent.methods.internalGetTorrentListRecentlyActive
import com.iregados.deckarr.api.qbittorrent.methods.internalPing
import com.iregados.deckarr.api.qbittorrent.methods.internalRemoveTorrent
import com.iregados.deckarr.api.qbittorrent.methods.internalResumeTorrent
import com.iregados.deckarr.api.qbittorrent.methods.internalStopTorrent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class QBitTorrentApi(
    private var protocol: URLProtocol,
    private var host: String,
    private var username: String,
    private var password: String,
    private var basePath: String
) : TorrentClientApi {
    private val log = Logger(
        config = loggerConfigInit(
            platformLogWriter(NoTagFormatter)
        ),
        tag = "com.iregados.deckarr.api.qbittorrent.QBitTorrentApi"
    )
    private var client = createClient(protocol, host)
    internal var rid: Long = 0
    override val config = QBitTorrentApiConfig(
        protocol,
        host,
        username,
        password
    )

    override fun close() {
        client.close()
    }


    private fun createClient(
        protocol: URLProtocol,
        host: String,
    ): HttpClient {
        return HttpClientFactory.create(
            json = Json {
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
                encodeDefaults = true
            },
            expectSuccess = false
        ) {
            install(HttpCookies)

            defaultRequest {
                url {
                    this.protocol = protocol
                    this.host = host
                }
                header("Accept", "*/*")
                header("Referer", "${protocol.name}://$host")
                header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
            }
        }
    }

    override suspend fun getTorrentListAndStats() = internalGetTorrentListAndStats()
    override suspend fun getTorrentListRecentlyActive() = internalGetTorrentListRecentlyActive()
    override suspend fun ping() = internalPing()
    override suspend fun stopTorrent(ids: List<String>) = internalStopTorrent(ids)
    override suspend fun resumeTorrent(ids: List<String>) = internalResumeTorrent(ids)
    override suspend fun removeTorrent(ids: List<String>, deleteLocalData: Boolean) =
        internalRemoveTorrent(ids, deleteLocalData)

    internal suspend inline fun <reified T> apiGet(
        path: String,
        parameters: Map<String, String> = emptyMap(),
        retry: Boolean = true,
    ): ApiResult<T> {
        var attempt = 0

        while (true) {
            try {
                val response = client.get("$basePath$path") {
                    parameters.forEach { (key, value) -> parameter(key, value) }
                }

                if (response.status.value == 403 && retry && attempt == 0) {
                    if (!login()) {
                        return ApiResult.Error(ApiError.Unauthorized("Login error"))
                    }
                    attempt++
                    continue
                }

                if (response.status.value != 200) {
                    return ApiResult.Error(
                        ApiError.Server(response.status.value, response.status.description)
                    )
                }

                return ApiResult.Success(response.body())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                log.e("apiGet $path error: ${e.message ?: "Unknown error"}")
                return ApiResult.Error(e.toApiError())
            }
        }
    }


    internal suspend inline fun <reified Req, reified Res> apiPost(
        path: String,
        body: Req,
        retry: Boolean = true,
    ): ApiResult<Res> {
        var attempt = 0

        while (true) {
            try {
                val response = client.post("$basePath$path") {
                    if (body is Map<*, *>) {
                        val parameters = Parameters.build {
                            body.forEach { (key, value) ->
                                if (key is String && value is String) {
                                    append(key, value)
                                }
                            }
                        }
                        setBody(FormDataContent(parameters))
                    } else {
                        setBody(body)
                    }
                }

                if (response.status.value == 403 && retry && attempt == 0) {
                    if (!login()) {
                        return ApiResult.Error(ApiError.Unauthorized("Login error"))
                    }
                    attempt++
                    continue
                }

                if (response.status.value != 200) {
                    return ApiResult.Error(
                        ApiError.Server(response.status.value, response.status.description)
                    )
                }

                return ApiResult.Success(response.body())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                log.e("apiPost $path error: ${e.message ?: "Unknown error"}")
                return ApiResult.Error(e.toApiError())
            }
        }
    }

    private suspend fun login(): Boolean {
        try {
            val response = client.post("$basePath/api/v2/auth/login") {
                setBody(FormDataContent(Parameters.build {
                    append("username", username)
                    append("password", password)
                }))

                header("Origin", "${protocol.name}://$host")
            }
            if (response.status.value !in listOf(200, 204)) return false
            return response.bodyAsText().trim().equals("Ok.", ignoreCase = true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            log.e("Login failed: ${e.message}")
            return false
        }
    }
}