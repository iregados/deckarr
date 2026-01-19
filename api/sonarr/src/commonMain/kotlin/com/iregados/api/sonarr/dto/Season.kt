package com.iregados.api.sonarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class Season(
    val seasonNumber: Long,
    val monitored: Boolean,
    val statistics: SeasonStatistics?,
)

@Serializable
data class SeasonStatistics(
    val episodeFileCount: Long,
    val episodeCount: Long,
    val totalEpisodeCount: Long,
    val sizeOnDisk: Long,
    val releaseGroups: List<String>,
    val percentOfEpisodes: Double,
    val previousAiring: String?,
    val nextAiring: String?,
)