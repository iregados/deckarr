package com.iregados.api.transmission.methods

import co.touchlab.kermit.Logger
import com.iregados.api.common.ApiResult
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.TorrentActivityResponse
import com.iregados.api.transmission.dto.internal.TorrentListRecentlyActiveRequest

suspend fun TransmissionApi.getTorrentListRecentlyActive(): ApiResult<TorrentActivityResponse> {
    val request = TorrentListRecentlyActiveRequest()
    return when (
        val response = rpcCall<TorrentListRecentlyActiveRequest, TorrentActivityResponse>(request)
    ) {
        is ApiResult.Success -> ApiResult.Success(response.data)
        is ApiResult.Error -> {
            Logger.e(response.message)
            ApiResult.Error(response.message)
        }
    }
}