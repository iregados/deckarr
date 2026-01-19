package com.iregados.api.transmission.dto

import com.iregados.api.transmission.serializers.TorrentActivityResponseSerializer
import kotlinx.serialization.Serializable

@Serializable(with = TorrentActivityResponseSerializer::class)
data class TorrentActivityResponse(
    val torrents: List<Torrent>,
    val removed: List<Int>
)