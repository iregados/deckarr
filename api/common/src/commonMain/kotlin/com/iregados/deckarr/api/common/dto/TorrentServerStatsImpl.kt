package com.iregados.deckarr.api.common.dto

import com.iregados.deckarr.api.common.interfaces.TorrentServerStats
import kotlinx.serialization.Serializable

@Serializable
data class TorrentServerStatsImpl(
    override val downloadedBytes: Long?,
    override val uploadedBytes: Long?,
    override val globalRatio: Float?,
) : TorrentServerStats
