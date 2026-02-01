package com.iregados.api.transmission.dto

import com.iregados.api.common.interfaces.Torrent
import kotlinx.serialization.Serializable

@Serializable
data class QBitTorrentActivityResponse(
    val torrents: List<Torrent>,
    val removed: List<String>
)
