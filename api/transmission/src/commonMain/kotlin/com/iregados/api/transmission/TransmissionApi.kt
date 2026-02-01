package com.iregados.api.transmission

import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.iregados.api.common.ApiResult
import com.iregados.api.common.interfaces.TorrentClientApi
import com.iregados.api.transmission.dto.TransmissionApiConfig
import com.iregados.api.transmission.methods.internalGetTorrentListAndStats
import com.iregados.api.transmission.methods.internalGetTorrentListRecentlyActive
import com.iregados.api.transmission.methods.internalPing
import com.iregados.api.transmission.methods.internalRemoveTorrent
import com.iregados.api.transmission.methods.internalResumeTorrent
import com.iregados.api.transmission.methods.internalStopTorrent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.Base64

class TransmissionApi(
    private var protocol: URLProtocol,
    private var host: String,
    private var username: String,
    private var password: String
) : TorrentClientApi {
    private val log = Logger(
        config = loggerConfigInit(
            platformLogWriter(NoTagFormatter)
        ),
        tag = "TransmissionApi"
    )
    private var client = createClient(protocol, host, username, password)
    private var sessionId: String? = null
    override val config = TransmissionApiConfig(
        protocol,
        host,
        username,
        password
    )


    private fun createClient(
        protocol: URLProtocol,
        host: String,
        username: String,
        password: String
    ): HttpClient {
        return HttpClient {
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
                header("Accept", "application/json")
                header("Content-Type", "application/json")
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    header(
                        "Authorization",
                        "Basic " + Base64
                            .getEncoder()
                            .encodeToString("${username}:${password}".encodeToByteArray())
                    )
                }
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

    internal suspend inline fun <reified Req, reified Res> rpcCall(
        body: Req
    ): ApiResult<Res> {
        try {
            val response = client.post("/rpc") {
                sessionId?.let { header("x-transmission-session-id", it) }
                setBody(body)
            }
            if (response.status.value == 409) {
                val newSessionId = response.headers["x-transmission-session-id"]
                if (newSessionId != null) {
                    sessionId = newSessionId
                    val retryResponse = client.post("/rpc") {
                        header("x-transmission-session-id", sessionId!!)
                        setBody(body)
                    }
                    return ApiResult.Success(retryResponse.body())
                }
            }
            return ApiResult.Success(response.body())
        } catch (e: Exception) {
            log.e("rpcCall error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.message ?: "Unknown error")
        }
    }
}