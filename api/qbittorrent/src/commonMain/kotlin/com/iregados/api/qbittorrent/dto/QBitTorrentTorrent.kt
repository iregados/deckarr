package com.iregados.api.qbittorrent.dto

import com.iregados.api.common.dto.TorrentImpl
import com.iregados.api.common.dto.TorrentStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class QBitTorrentTorrent(
    val id: String = "",
    val name: String?,
    @SerialName("added_on") val addedDate: Long?,
    val state: String?,
    @SerialName("size") val totalSize: Long?,
    @SerialName("progress") val percentDone: Float?,
    @SerialName("dlspeed") val rateDownload: Int?,
    @SerialName("upspeed") val rateUpload: Int?,
    @SerialName("uploaded") val uploadedEver: Long?,
    @SerialName("downloaded") val downloadedEver: Long?,
    val eta: Int? = null,
)

internal fun QBitTorrentTorrent.toTorrent() = TorrentImpl(
    id = id,
    name = name,
    addedDate = addedDate,
    status = mapQBitTorrentStatus(state, eta, rateUpload, rateDownload),
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

internal fun mapQBitTorrentStatus(
    status: String?,
    eta: Int?,
    rateUpload: Int?,
    rateDownload: Int?
): Int? {
    return when (status) {
        // null
        null -> null

        // Stopped / Paused
        "pausedUP", "pausedDL", "error", "missingFiles" -> TorrentStatus.STOPPED

        // Verifying Data
        "checkingUP", "checkingDL", "checkingResumeData" -> TorrentStatus.VERIFYING_LOCAL_DATA

        // Queued to Download
        "queuedDL" -> TorrentStatus.QUEUED_TO_DOWNLOAD

        // Downloading
        "downloading", "stalledDL", "forcedDL", "metaDL", "allocating", "moving" -> TorrentStatus.DOWNLOADING

        // Queued to Seed
        "queuedUP" -> TorrentStatus.QUEUED_TO_SEED

        // Seeding
        "uploading", "stalledUP", "forcedUP" -> TorrentStatus.SEEDING

        // Default to stopped for unknown states
        else -> TorrentStatus.STOPPED
    }
}