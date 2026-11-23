package com.iregados.deckarr.api.transmission.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.transmission.TransmissionApi
import com.iregados.deckarr.api.transmission.dto.internal.TorrentStopRequest
import kotlinx.serialization.json.JsonObject

suspend fun TransmissionApi.internalStopTorrent(ids: List<String>): ApiResult<Boolean> {
    val request = TorrentStopRequest(
        arguments = TorrentStopRequest.Arguments(ids.map { it.toInt() })
    )
    return when (val response = rpcCall<TorrentStopRequest, JsonObject>(request)) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}