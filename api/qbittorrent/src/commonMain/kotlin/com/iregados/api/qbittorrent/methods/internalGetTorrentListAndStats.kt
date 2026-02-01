package com.iregados.api.qbittorrent.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.common.dto.TorrentInitialDataImpl
import com.iregados.api.common.interfaces.Torrent
import com.iregados.api.common.interfaces.TorrentInitialData
import com.iregados.api.qbittorrent.QBitTorrentApi
import com.iregados.api.qbittorrent.dto.internal.QBitTorrentMainDataResponse
import com.iregados.api.qbittorrent.dto.toTorrent
import com.iregados.api.qbittorrent.dto.toTorrentServerStats

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
