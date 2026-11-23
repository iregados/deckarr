package com.iregados.deckarr.api.transmission.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.transmission.TransmissionApi
import com.iregados.deckarr.api.transmission.dto.internal.TorrentRemoveRequest
import kotlinx.serialization.json.JsonObject

suspend fun TransmissionApi.internalRemoveTorrent(
    ids: List<String>,
    deleteLocalData: Boolean
): ApiResult<Boolean> {
    val request = TorrentRemoveRequest(
        arguments = TorrentRemoveRequest.Arguments(
            ids = ids.map { it.toInt() },
            deleteLocalData = deleteLocalData
        )
    )
    return when (val response = rpcCall<TorrentRemoveRequest, JsonObject>(request)) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}