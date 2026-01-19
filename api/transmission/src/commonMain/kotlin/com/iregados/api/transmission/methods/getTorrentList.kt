package com.iregados.api.transmission.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.Torrent
import com.iregados.api.transmission.dto.TorrentActivityResponse
import com.iregados.api.transmission.dto.internal.TorrentListRequest

suspend fun TransmissionApi.getTorrentList(): ApiResult<List<Torrent>> {
    val request = TorrentListRequest()
    return when (
        val response = rpcCall<TorrentListRequest, TorrentActivityResponse>(request)
    ) {
        is ApiResult.Success -> ApiResult.Success(response.data.torrents)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}