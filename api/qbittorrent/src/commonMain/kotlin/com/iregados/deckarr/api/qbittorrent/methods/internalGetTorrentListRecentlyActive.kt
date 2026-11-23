package com.iregados.deckarr.api.qbittorrent.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.dto.TorrentActivityResponseImpl
import com.iregados.deckarr.api.common.interfaces.TorrentActivityResponse
import com.iregados.deckarr.api.qbittorrent.QBitTorrentApi
import com.iregados.deckarr.api.qbittorrent.dto.internal.QBitTorrentMainDataResponse
import com.iregados.deckarr.api.qbittorrent.dto.toTorrent

suspend fun QBitTorrentApi.internalGetTorrentListRecentlyActive(): ApiResult<TorrentActivityResponse> {

    val response = apiGet<
            QBitTorrentMainDataResponse
            >("/api/v2/sync/maindata?rid=$rid")

    return when (response) {
        is ApiResult.Success -> {
            this.rid = response.data.rid
            ApiResult.Success(
                TorrentActivityResponseImpl(
                    torrents = response.data.torrents.entries.map { (hash, qBitTorrentTorrent) ->
                        qBitTorrentTorrent.toTorrent().copy(id = hash)
                    },
                    removed = response.data.torrentsRemoved,
                )
            )
        }

        is ApiResult.Error -> {
            ApiResult.Error(response.message)
        }
    }
}