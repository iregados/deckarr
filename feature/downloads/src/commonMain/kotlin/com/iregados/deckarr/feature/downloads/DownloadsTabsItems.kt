package com.iregados.deckarr.feature.downloads

import com.iregados.api.common.interfaces.Torrent

sealed interface DownloadsTabsItems {
    val title: String
    val items: List<Torrent>?

    data class Active(
        override val title: String = "Active",
        override val items: List<Torrent>?,
    ) : DownloadsTabsItems

    data class All(
        override val title: String = "All",
        override val items: List<Torrent>?,
    ) : DownloadsTabsItems

    data class Downloading(
        override val title: String = "Downloading",
        override val items: List<Torrent>?,
    ) : DownloadsTabsItems
}