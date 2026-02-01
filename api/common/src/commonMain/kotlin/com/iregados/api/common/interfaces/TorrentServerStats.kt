package com.iregados.api.common.interfaces

interface TorrentServerStats {
    val downloadedBytes: Long?
    val uploadedBytes: Long?
    val globalRatio: Float?
}