package com.iregados.api.qbittorrent.dto.internal

import com.iregados.api.qbittorrent.dto.QBitTorrentServerStats
import com.iregados.api.qbittorrent.dto.QBitTorrentTorrent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class QBitTorrentMainDataResponse(
    val rid: Long,
    val torrents: Map<String, QBitTorrentTorrent> = emptyMap(),
    @SerialName("torrents_removed") val torrentsRemoved: List<String> = emptyList(),
    @SerialName("server_state") val serverState: QBitTorrentServerStats?
)

