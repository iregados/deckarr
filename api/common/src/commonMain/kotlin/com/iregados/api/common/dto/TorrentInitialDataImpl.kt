package com.iregados.api.common.dto

import com.iregados.api.common.interfaces.Torrent
import com.iregados.api.common.interfaces.TorrentInitialData
import com.iregados.api.common.interfaces.TorrentServerStats
import kotlinx.serialization.Serializable

@Serializable
data class TorrentInitialDataImpl(
    override val torrentList: List<Torrent>?,
    override val torrentSessionStats: TorrentServerStats?,
) : TorrentInitialData
