package com.iregados.api.qbittorrent.dto

import com.iregados.api.common.dto.TorrentServerStatsImpl
import com.iregados.api.common.interfaces.TorrentServerStats
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class QBitTorrentServerStats(
    @SerialName("alltime_dl") val allTimeDl: Long?,
    @SerialName("alltime_ul") val allTimeUl: Long?,
)

internal fun QBitTorrentServerStats.toTorrentServerStats(): TorrentServerStats =
    TorrentServerStatsImpl(
        downloadedBytes = allTimeDl,
        uploadedBytes = allTimeUl,
        globalRatio = allTimeUl?.toFloat()?.div(allTimeDl ?: 1L)
    )
