package com.iregados.deckarr.api.transmission.dto

import com.iregados.deckarr.api.common.dto.TorrentServerStatsImpl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransmissionSessionStats(
    val activeTorrentCount: Long,
    val downloadSpeed: Long?,
    val pausedTorrentCount: Long?,
    val torrentCount: Long?,
    val uploadSpeed: Long?,
    @SerialName("cumulative-stats") val cumulativeStats: TransmissionCumulativeStats,
)


@Serializable
data class TransmissionCumulativeStats(
    val downloadedBytes: Long,
    val filesAdded: Long,
    val secondsActive: Long,
    val sessionCount: Long,
    val uploadedBytes: Long,
)

internal fun TransmissionSessionStats.toTorrentSessionStats() = TorrentServerStatsImpl(
    downloadedBytes = this.cumulativeStats.downloadedBytes,
    uploadedBytes = this.cumulativeStats.uploadedBytes,
    globalRatio = if (cumulativeStats.downloadedBytes > 0) {
        cumulativeStats.uploadedBytes.toFloat() / cumulativeStats.downloadedBytes
    } else {
        null
    }
)