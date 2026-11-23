package com.iregados.deckarr.api.qbittorrent.dto

import com.iregados.deckarr.api.common.dto.TorrentServerStatsImpl
import com.iregados.deckarr.api.common.interfaces.TorrentServerStats
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
        globalRatio = if (allTimeDl != null && allTimeDl > 0 && allTimeUl != null) {
            allTimeUl.toFloat() / allTimeDl
        } else {
            null
        }
    )
