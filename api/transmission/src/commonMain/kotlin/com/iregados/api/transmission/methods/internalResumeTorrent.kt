package com.iregados.api.transmission.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.internal.TorrentStartRequest

suspend fun TransmissionApi.internalResumeTorrent(
    ids: List<String>
): ApiResult<Boolean> {
    val request = TorrentStartRequest(
        arguments = TorrentStartRequest.Arguments(ids.map { it.toInt() })
    )
    return when (val response = rpcCall<TorrentStartRequest, Any>(request)) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}