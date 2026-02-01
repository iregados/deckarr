package com.iregados.api.qbittorrent.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.qbittorrent.QBitTorrentApi

suspend fun QBitTorrentApi.internalRemoveTorrent(
    ids: List<String>,
    deleteLocalData: Boolean
): ApiResult<Boolean> {
    val hashes = ids.joinToString(separator = "|")
    val response = apiPost<
            Map<String, String>,
            String
            >(
        "/api/v2/torrents/delete",
        mapOf(
            "hashes" to hashes,
            "deleteFiles" to deleteLocalData.toString()
        )
    )
    return when (response) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}