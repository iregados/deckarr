package com.iregados.deckarr.api.qbittorrent.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.qbittorrent.QBitTorrentApi

suspend fun QBitTorrentApi.internalPing(): ApiResult<Boolean> {
    val response = apiGet<
            String
            >("/api/v2/app/version")

    return when (response) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}
