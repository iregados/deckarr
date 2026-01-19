package com.iregados.api.sonarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val seriesId: Long,
    val tvdbId: Long,
    val episodeFileId: Long,
    val seasonNumber: Long,
    val episodeNumber: Long,
    val title: String?,
    val airDate: String?,
    val runtime: Long?,
    val overview: String?,
    val hasFile: Boolean,
    val monitored: Boolean,
    val id: Long,
    val finaleType: String?,
)
