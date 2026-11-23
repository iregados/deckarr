package com.iregados.deckarr.api.transmission.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.interfaces.TorrentActivityResponse
import com.iregados.deckarr.api.transmission.TransmissionApi
import com.iregados.deckarr.api.transmission.dto.TransmissionActivityResponse
import com.iregados.deckarr.api.transmission.dto.internal.TorrentListRecentlyActiveRequest

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