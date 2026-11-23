package com.iregados.deckarr.api.common.dto

import com.iregados.deckarr.api.common.interfaces.Torrent
import com.iregados.deckarr.api.common.interfaces.TorrentActivityResponse

data class TorrentActivityResponseImpl(
    override val torrents: List<Torrent>,
    override val removed: List<String>
) : TorrentActivityResponse
