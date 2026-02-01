package com.iregados.api.transmission.dto

import com.iregados.api.common.dto.TorrentServerStatsImpl
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
    globalRatio = this.cumulativeStats.uploadedBytes.toFloat() / this.cumulativeStats.downloadedBytes
)