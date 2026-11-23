package com.iregados.deckarr.api.common.dto

import com.iregados.deckarr.api.common.interfaces.Torrent

data class TorrentImpl(
    override val id: String = "",
    override val name: String?,
    override val addedDate: Long?,
    override val status: Int?,
    override val totalSize: Long?,
    override val percentDone: Float?,
    override val isFinished: Boolean?,
    override val isStalled: Boolean?,
    override val rateDownload: Int?,
    override val rateUpload: Int?,
    override val uploadedEver: Long?,
    override val downloadedEver: Long?,
    override val eta: Int?,
) : Torrent
