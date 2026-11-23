package com.iregados.deckarr.feature.downloads.utils

import com.iregados.deckarr.api.common.interfaces.Torrent
import com.iregados.deckarr.feature.downloads.DownloadsState

fun DownloadsState.withTorrents(torrents: List<Torrent>?): DownloadsState = copy(
    torrents = torrents,
    activeTorrents = torrents?.filter { it.rateDownload != 0 || it.rateUpload != 0 },
    downloadingTorrents = torrents?.filter { (it.percentDone ?: 0f) < 1f },
    totalDownloadSpeed = torrents?.sumOf { it.rateDownload ?: 0 },
    totalUploadSpeed = torrents?.sumOf { it.rateUpload ?: 0 },
)