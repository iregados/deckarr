package com.iregados.deckarr.core.util.extension

import com.iregados.api.transmission.dto.TorrentStatus

fun Int?.toFormatedStatus(): String = when (this) {
    TorrentStatus.STOPPED -> "Stopped"
    TorrentStatus.DOWNLOADING -> "Downloading"
    TorrentStatus.SEEDING -> "Seeding"
    TorrentStatus.QUEUED_TO_DOWNLOAD -> "Queued to download"
    TorrentStatus.QUEUED_TO_SEED -> "Queued to seed"
    TorrentStatus.QUEUED_TO_VERIFY_LOCAL_DATA -> "Queued to verify local data"
    TorrentStatus.VERIFYING_LOCAL_DATA -> "Verifying local data"
    else -> "Unknown"
}