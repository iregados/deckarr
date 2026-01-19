package com.iregados.api.transmission.dto.internal

import kotlinx.serialization.Serializable

@Serializable
internal data class TorrentStartRequest(
    val method: String = "torrent-start",
    val arguments: Arguments
) {
    @Serializable
    data class Arguments(
        val ids: List<Int>
    )
}