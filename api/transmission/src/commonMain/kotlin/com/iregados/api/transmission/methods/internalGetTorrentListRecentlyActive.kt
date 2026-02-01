package com.iregados.api.transmission.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.common.interfaces.TorrentActivityResponse
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.TransmissionActivityResponse
import com.iregados.api.transmission.dto.internal.TorrentListRecentlyActiveRequest

suspend fun TransmissionApi.internalGetTorrentListRecentlyActive(): ApiResult<TorrentActivityResponse> {
    val request = TorrentListRecentlyActiveRequest()
    return when (
        val response =
            rpcCall<TorrentListRecentlyActiveRequest, TransmissionActivityResponse>(
                request
            )
    ) {
        is ApiResult.Success -> ApiResult.Success(response.data)
        is ApiResult.Error -> {
            ApiResult.Error(response.message)
        }
    }
}