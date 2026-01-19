package com.iregados.api.transmission.dto

object TorrentStatus {
    const val STOPPED = 0
    const val QUEUED_TO_VERIFY_LOCAL_DATA = 1
    const val VERIFYING_LOCAL_DATA = 2
    const val QUEUED_TO_DOWNLOAD = 3
    const val DOWNLOADING = 4
    const val QUEUED_TO_SEED = 5
    const val SEEDING = 6
}