package com.iregados.deckarr.api.transmission.dto

import com.iregados.deckarr.api.common.interfaces.Torrent
import com.iregados.deckarr.api.common.interfaces.TorrentActivityResponse
import com.iregados.deckarr.api.transmission.serializers.TransmissionTorrentActivityResponseSerializer
import kotlinx.serialization.Serializable

@Serializable(with = TransmissionTorrentActivityResponseSerializer::class)
data class TransmissionActivityResponse(
    override val torrents: List<Torrent>,
    override val removed: List<String>
) : TorrentActivityResponse