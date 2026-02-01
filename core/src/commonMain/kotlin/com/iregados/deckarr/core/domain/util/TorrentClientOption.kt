package com.iregados.deckarr.core.domain.util

import kotlinx.serialization.Serializable

@Serializable
sealed interface TorrentClientOption {
    @Serializable
    object QBitTorrent : TorrentClientOption

    @Serializable
    object Transmission : TorrentClientOption
}