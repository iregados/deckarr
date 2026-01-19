package com.iregados.api.transmission.dto.internal

import kotlinx.serialization.Serializable

@Serializable
internal data class TorrentStopRequest(
    val method: String = "torrent-stop",
    val arguments: Arguments
) {
    @Serializable
    data class Arguments(
        val ids: List<Int>
    )
}