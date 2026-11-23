package com.iregados.deckarr.api.qbittorrent.methods

import com.iregados.deckarr.api.common.ApiError
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.qbittorrent.QBitTorrentApi

suspend fun QBitTorrentApi.internalStopTorrent(
    ids: List<String>
): ApiResult<Boolean> {
    val hashes = ids.joinToString(separator = "|")
    val body = mapOf("hashes" to hashes)
    val response = apiPost<Map<String, String>, String>("/api/v2/torrents/stop", body)

    // qBittorrent 4.x has no torrents/stop, only torrents/pause
    val finalResponse =
        if (response is ApiResult.Error && (response.error as? ApiError.Server)?.code == 404) {
            apiPost<Map<String, String>, String>("/api/v2/torrents/pause", body)
        } else {
            response
        }

    return when (finalResponse) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(finalResponse.message)
    }
}