package com.iregados.api.common.dto

import com.iregados.api.common.interfaces.Torrent
import com.iregados.api.common.interfaces.TorrentActivityResponse

data class TorrentActivityResponseImpl(
    override val torrents: List<Torrent>,
    override val removed: List<String>
) : TorrentActivityResponse
