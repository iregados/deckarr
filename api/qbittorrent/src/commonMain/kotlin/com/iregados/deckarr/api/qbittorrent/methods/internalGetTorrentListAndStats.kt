package com.iregados.deckarr.api.qbittorrent.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.dto.TorrentInitialDataImpl
import com.iregados.deckarr.api.common.interfaces.Torrent
import com.iregados.deckarr.api.common.interfaces.TorrentInitialData
import com.iregados.deckarr.api.qbittorrent.QBitTorrentApi
import com.iregados.deckarr.api.qbittorrent.dto.internal.QBitTorrentMainDataResponse
import com.iregados.deckarr.api.qbittorrent.dto.toTorrent
import com.iregados.deckarr.api.qbittorrent.dto.toTorrentServerStats

suspend fun QBitTorrentApi.internalGetTorrentListAndStats(): ApiResult<TorrentInitialData> {
    val response = apiGet<
            QBitTorrentMainDataResponse
            >("/api/v2/sync/maindata?rid=0")

    return when (response) {
        is ApiResult.Success -> {
            this.rid = response.data.rid
            val torrentsList: List<Torrent> =
                response.data.torrents.entries.map { (hash, qBitTorrentTorrent) ->
                    qBitTorrentTorrent.toTorrent().copy(id = hash)
                }
            ApiResult.Success(
                TorrentInitialDataImpl(
                    torrentList = torrentsList,
                    torrentSessionStats = response.data.serverState?.toTorrentServerStats(),
                )
            )
        }

        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}
