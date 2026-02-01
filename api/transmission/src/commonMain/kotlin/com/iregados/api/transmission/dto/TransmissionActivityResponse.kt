package com.iregados.api.transmission.dto

import com.iregados.api.common.interfaces.Torrent
import com.iregados.api.common.interfaces.TorrentActivityResponse
import com.iregados.api.transmission.serializers.TransmissionTorrentActivityResponseSerializer
import kotlinx.serialization.Serializable

@Serializable(with = TransmissionTorrentActivityResponseSerializer::class)
data class TransmissionActivityResponse(
    override val torrents: List<Torrent>,
    override val removed: List<String>
) : TorrentActivityResponse