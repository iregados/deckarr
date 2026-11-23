package com.iregados.deckarr.api.transmission

import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.iregados.deckarr.api.common.ApiError
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.interfaces.TorrentClientApi
import com.iregados.deckarr.api.common.util.HttpClientFactory
import com.iregados.deckarr.api.common.util.toApiError
import com.iregados.deckarr.api.transmission.dto.TransmissionApiConfig
import com.iregados.deckarr.api.transmission.methods.internalGetTorrentListAndStats
import com.iregados.deckarr.api.transmission.methods.internalGetTorrentListRecentlyActive
import com.iregados.deckarr.api.transmission.methods.internalPing
import com.iregados.deckarr.api.transmission.methods.internalRemoveTorrent
import com.iregados.deckarr.api.transmission.methods.internalResumeTorrent
import com.iregados.deckarr.api.transmission.methods.internalStopTorrent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Base64
import kotlin.coroutines.cancellation.CancellationException

class TransmissionApi(
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
        tag = "com.iregados.deckarr.api.transmission.TransmissionApi"
    )
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    private var client = createClient(protocol, host, username, password)
    private var sessionId: String? = null
    override val config = TransmissionApiConfig(
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
        username: String,
        password: String,
    ): HttpClient {
        return HttpClientFactory.create(
            json = json,
            expectSuccess = false
        ) {
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

    private inline fun <reified Res> decodeRpcResponse(text: String): ApiResult<Res> {
        val result = json.parseToJsonElement(text).jsonObject["result"]?.jsonPrimitive?.content
        return if (result != null && result != "success") {
            ApiResult.Error(ApiError.Unknown(result))
        } else {
            ApiResult.Success(json.decodeFromString<Res>(text))
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
            val response = client.post("$basePath/rpc") {
                sessionId?.let { header("x-transmission-session-id", it) }
                setBody(body)
            }
            if (response.status.value == 409) {
                val newSessionId = response.headers["x-transmission-session-id"]
                if (newSessionId != null) {
                    sessionId = newSessionId
                    val retryResponse = client.post("$basePath/rpc") {
                        header("x-transmission-session-id", sessionId!!)
                        setBody(body)
                    }
                    if (!retryResponse.status.isSuccess()) {
                        return ApiResult.Error(
                            ApiError.Server(
                                retryResponse.status.value,
                                retryResponse.status.description
                            )
                        )
                    }
                    return decodeRpcResponse<Res>(retryResponse.bodyAsText())
                }
            }
            if (!response.status.isSuccess()) {
                return ApiResult.Error(
                    ApiError.Server(response.status.value, response.status.description)
                )
            }
            return decodeRpcResponse<Res>(response.bodyAsText())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            log.e("rpcCall error: ${e.message ?: "Unknown error"}")
            return ApiResult.Error(e.toApiError())
        }
    }
}