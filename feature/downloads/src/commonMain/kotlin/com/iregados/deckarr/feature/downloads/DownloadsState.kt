package com.iregados.deckarr.feature.downloads

import com.iregados.api.common.interfaces.Torrent
import com.iregados.api.common.interfaces.TorrentServerStats

data class DownloadsState(
    val isLoading: Boolean = true,
    val isUpdatingTorrentId: String? = null,
    val needsConfiguration: Boolean = false,
    val torrents: List<Torrent>? = null,
    val activeTorrents: List<Torrent>? = null,
    val downloadingTorrents: List<Torrent>? = null,
    val selectedTab: DownloadsTabsItems = DownloadsTabsItems.All(items = null),
    val totalDownloadSpeed: Int? = null,
    val totalUploadSpeed: Int? = null,
    val stats: TorrentServerStats? = null,
)

