package com.iregados.deckarr.api.qbittorrent.dto

import com.iregados.deckarr.api.common.interfaces.Torrent
import kotlinx.serialization.Serializable

@Serializable
data class QBitTorrentActivityResponse(
    val torrents: List<Torrent>,
    val removed: List<String>
)
