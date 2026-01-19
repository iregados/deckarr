package com.iregados.api.transmission.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.internal.TorrentStopRequest

suspend fun TransmissionApi.stopTorrent(ids: List<Int>): ApiResult<Boolean> {
    val request = TorrentStopRequest(arguments = TorrentStopRequest.Arguments(ids))
    return when (val response = rpcCall<TorrentStopRequest, Any>(request)) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}