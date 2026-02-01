package com.iregados.api.qbittorrent.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.qbittorrent.QBitTorrentApi

suspend fun QBitTorrentApi.internalPing(): ApiResult<Boolean> {
    val response = apiGet<
            String
            >("/api/v2/app/version")

    return when (response) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}
