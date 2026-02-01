package com.iregados.api.qbittorrent.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.qbittorrent.QBitTorrentApi

suspend fun QBitTorrentApi.internalStopTorrent(
    ids: List<String>
): ApiResult<Boolean> {
    val hashes = ids.joinToString(separator = "|")
    val response =
        apiPost<
                Map<String, String>,
                String
                >("/api/v2/torrents/stop", mapOf("hashes" to hashes))
    return when (response) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}