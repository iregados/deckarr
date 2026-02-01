package com.iregados.deckarr.feature.downloads

sealed class DownloadsEvent() {
    data class SetStopTorrent(
        val torrentId: String,
    ) : DownloadsEvent()

    data class SetResumeTorrent(
        val torrentId: String,
    ) : DownloadsEvent()

    data class SetRemoveTorrent(
        val torrentId: String,
        val deleteFiles: Boolean,
    ) : DownloadsEvent()

    data class SetSelectedTab(
        val tab: DownloadsTabsItems,
    ) : DownloadsEvent()
}
