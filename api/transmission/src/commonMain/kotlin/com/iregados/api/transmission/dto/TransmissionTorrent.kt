package com.iregados.api.transmission.dto

import com.iregados.api.common.dto.TorrentImpl
import com.iregados.api.common.interfaces.Torrent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransmissionTorrent(
    override val id: String,
    override val name: String?,
    @SerialName("added-date") override val addedDate: Long? = null,
    override val status: Int,
    @SerialName("total-size") override val totalSize: Long,
    @SerialName("percent-done") override val percentDone: Float?,
    @SerialName("is-finished") override val isFinished: Boolean? = null,
    @SerialName("is-stalled") override val isStalled: Boolean? = null,
    @SerialName("rate-download") override val rateDownload: Int? = null,
    @SerialName("rate-upload") override val rateUpload: Int? = null,
    @SerialName("uploaded-ever") override val uploadedEver: Long? = null,
    @SerialName("downloaded-ever") override val downloadedEver: Long? = null,
    override val eta: Int? = null,
) : Torrent

internal fun TransmissionTorrent.toTorrent() = TorrentImpl(
    id = id,
    name = name,
    addedDate = addedDate,
    status = status,
    totalSize = totalSize,
    percentDone = percentDone,
    rateDownload = rateDownload,
    rateUpload = rateUpload,
    uploadedEver = uploadedEver,
    downloadedEver = downloadedEver,
    eta = eta,
    isFinished = percentDone == 1f,
    isStalled = false
)