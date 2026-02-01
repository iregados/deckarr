package com.iregados.api.qbittorrent

import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.iregados.api.common.ApiResult
import com.iregados.api.common.interfaces.TorrentClientApi
import com.iregados.api.qbittorrent.dto.QBitTorrentApiConfig
import com.iregados.api.qbittorrent.methods.internalGetTorrentListAndStats
import com.iregados.api.qbittorrent.methods.internalGetTorrentListRecentlyActive
import com.iregados.api.qbittorrent.methods.internalPing
import com.iregados.api.qbittorrent.methods.internalRemoveTorrent
import com.iregados.api.qbittorrent.methods.internalResumeTorrent
import com.iregados.api.qbittorrent.methods.internalStopTorrent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class QBitTorrentApi(
    private var protocol: URLProtocol,
    private var host: String,
    private var username: String,
    private var password: String
) : TorrentClientApi {
    private val log = Logger(
        config = loggerConfigInit(
            platformLogWriter(NoTagFormatter)
        ),
        tag = "QBitTorrentApi"
    )
    private var client = createClient(protocol, host)
    internal var rid: Long = 0
    override val config = QBitTorrentApiConfig(
        protocol,
        host,
        username,
        password
    )


    private fun createClient(
        protocol: URLProtocol,
        host: String,
    ): HttpClient {
        return HttpClient {
            install(HttpCookies)
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 30_000
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                        encodeDefaults = true
                    }
                )
            }
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
        path: String
    ): ApiResult<T> {
        try {
            val response = client.get(path)
            if (response.status.value == 403) {
                if (!login()) {
                    return ApiResult.Error("Login error")
                }
                val newResponse = client.get(path)
                if (newResponse.status.value != 200) {
                    return ApiResult.Error("${response.status.value}")
                }
                return ApiResult.Success(newResponse.body())
            }
            if (response.status.value != 200) {
                throw Exception("Error ${response.status.value}")
            }
            return ApiResult.Success(response.body())
        } catch (e: Exception) {
            log.e("apiGet $path error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    internal suspend inline fun <reified Req, reified Res> apiPost(
        path: String,
        body: Req,
    ): ApiResult<Res> {
        try {
            val response = client.post(path) {
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
            if (response.status.value == 403) {
                if (!login()) {
                    return ApiResult.Error("Login error")
                }
                val newResponse = client.post(path) {
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
                if (newResponse.status.value != 200) {
                    return ApiResult.Error("${response.status.value}")
                }
                return ApiResult.Success(newResponse.body())
            }
            if (response.status.value != 200) {
                throw Exception("Error ${response.status.value}")
            }
            return ApiResult.Success(response.body())
        } catch (e: Exception) {
            log.e("apiPost $path error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun login(): Boolean {
        try {
            val response = client.post("/api/v2/auth/login") {
                setBody(FormDataContent(Parameters.build {
                    append("username", username)
                    append("password", password)
                }))

                header("Origin", "${protocol.name}://$host")
            }
            return response.status.value in listOf(200, 204)
        } catch (e: Exception) {
            log.e("Login failed: ${e.message}")
            return false
        }
    }
}