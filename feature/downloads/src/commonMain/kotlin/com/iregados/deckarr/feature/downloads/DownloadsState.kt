package com.iregados.deckarr.feature.downloads

import com.iregados.api.transmission.dto.SessionStats
import com.iregados.api.transmission.dto.Torrent

data class DownloadsState(
    val isLoading: Boolean = true,
    val isUpdatingTorrentId: Int? = null,
    val needsConfiguration: Boolean = false,
    val torrents: List<Torrent>? = null,
    val activeTorrents: List<Torrent>? = null,
    val downloadingTorrents: List<Torrent>? = null,
    val selectedTab: DownloadsTabsItems = DownloadsTabsItems.All(items = null),
    val totalDownloadSpeed: Int? = null,
    val totalUploadSpeed: Int? = null,
    val stats: SessionStats? = null,
)

