package com.iregados.api.transmission.dto.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TorrentRemoveRequest(
    val method: String = "torrent-remove",
    val arguments: Arguments
) {
    @Serializable
    data class Arguments(
        val ids: List<Int>,
        @SerialName("delete-local-data") val deleteLocalData: Boolean
    )
}