package com.iregados.deckarr.api.common.dto

import com.iregados.deckarr.api.common.interfaces.Torrent
import com.iregados.deckarr.api.common.interfaces.TorrentInitialData
import com.iregados.deckarr.api.common.interfaces.TorrentServerStats
import kotlinx.serialization.Serializable

@Serializable
data class TorrentInitialDataImpl(
    override val torrentList: List<Torrent>?,
    override val torrentSessionStats: TorrentServerStats?,
) : TorrentInitialData
