package com.iregados.deckarr.api.qbittorrent.dto

import com.iregados.deckarr.api.common.dto.TorrentImpl
import com.iregados.deckarr.api.common.dto.TorrentStatus
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
    status = mapQBitTorrentStatus(state),
    totalSize = totalSize,
    percentDone = percentDone,
    rateDownload = rateDownload,
    rateUpload = rateUpload,
    uploadedEver = uploadedEver,
    downloadedEver = downloadedEver,
    eta = if (eta != null && eta >= 8640000) -1 else eta,
    isFinished = percentDone?.let { it == 1f },
    isStalled = when (state) {
        null -> null
        "stalledDL", "stalledUP" -> true
        else -> false
    }
)

internal fun mapQBitTorrentStatus(
    status: String?,
): Int? {
    return when (status) {
        // null
        null -> null

        // Stopped / Paused
        "stoppedUP", "stoppedDL", "pausedUP", "pausedDL", "error", "missingFiles" -> TorrentStatus.STOPPED

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