package com.iregados.api.transmission.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.internal.TorrentRemoveRequest

suspend fun TransmissionApi.removeTorrent(
    ids: List<Int>,
    deleteLocalData: Boolean
): ApiResult<Boolean> {
    val request = TorrentRemoveRequest(
        arguments = TorrentRemoveRequest.Arguments(
            ids = ids,
            deleteLocalData = deleteLocalData
        )
    )
    return when (val response = rpcCall<TorrentRemoveRequest, Any>(request)) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}