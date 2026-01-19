package com.iregados.deckarr.feature.downloads

sealed class DownloadsEvent() {
    data class SetStopTorrent(
        val torrentId: Int,
    ) : DownloadsEvent()

    data class SetResumeTorrent(
        val torrentId: Int,
    ) : DownloadsEvent()

    data class SetRemoveTorrent(
        val torrentId: Int,
        val deleteFiles: Boolean,
    ) : DownloadsEvent()

    data class SetSelectedTab(
        val tab: DownloadsTabsItems,
    ) : DownloadsEvent()
}
