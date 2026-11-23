package com.iregados.deckarr.core.domain.dto

import com.iregados.deckarr.api.common.interfaces.Torrent
import com.iregados.deckarr.api.common.interfaces.TorrentServerStats

data class DownloadsDataState(
    val isLoading: Boolean = true,
    val isConfigLoaded: Boolean = false,
    val needsConfiguration: Boolean = false,
    val error: String? = null,
    val torrents: List<Torrent>? = null,
    val stats: TorrentServerStats? = null,
)