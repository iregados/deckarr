package com.iregados.api.common.interfaces


interface Torrent {
    val id: String
    val name: String?
    val addedDate: Long?
    val status: Int?
    val totalSize: Long?
    val percentDone: Float?
    val isFinished: Boolean?
    val isStalled: Boolean?
    val rateDownload: Int?
    val rateUpload: Int?
    val uploadedEver: Long?
    val downloadedEver: Long?
    val eta: Int?
}