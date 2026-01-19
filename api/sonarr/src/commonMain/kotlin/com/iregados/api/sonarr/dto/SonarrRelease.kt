package com.iregados.api.sonarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class SonarrRelease(
    val guid: String,
    val quality: Quality,
    val qualityWeight: Long,
    val age: Long,
    val ageHours: Double,
    val ageMinutes: Double,
    val size: Long,
    val indexerId: Long,
    val indexer: String,
    val releaseHash: String?,
    val title: String,
    val fullSeason: Boolean,
    val sceneSource: Boolean,
    val seasonNumber: Long,
    val languages: List<Language>,
    val languageWeight: Long,
    val seriesTitle: String?,
    val episodeNumbers: List<Long>,
    val mappedSeasonNumber: Long?,
    val mappedEpisodeNumbers: List<Long>,
    val mappedSeriesId: Long?,
    val mappedEpisodeInfo: List<MappedEpisodeInfo>,
    val approved: Boolean,
    val temporarilyRejected: Boolean,
    val rejected: Boolean,
    val tvdbId: Long,
    val tvRageId: Long,
    val rejections: List<String>,
    val publishDate: String,
    val commentUrl: String,
    val downloadUrl: String,
    val infoUrl: String,
    val episodeRequested: Boolean,
    val downloadAllowed: Boolean,
    val releaseWeight: Long,
    val magnetUrl: String,
    val infoHash: String,
    val seeders: Long,
    val leechers: Long,
    val protocol: String,
    val indexerFlags: Long,
    val isDaily: Boolean,
    val isAbsoluteNumbering: Boolean,
    val isPossibleSpecialEpisode: Boolean,
    val special: Boolean,
    val releaseGroup: String?,
    val imdbId: String?,
)

@Serializable
data class Language(
    val id: Long,
    val name: String,
)

@Serializable
data class Quality(
    val quality: QualityData,
    val revision: Revision,
)

@Serializable
data class QualityData(
    val id: Long,
    val name: String,
    val source: String,
    val resolution: Long,
)

@Serializable
data class Revision(
    val version: Long,
    val real: Long,
    val isRepack: Boolean,
)

@Serializable
data class MappedEpisodeInfo(
    val id: Long,
    val seasonNumber: Long,
    val episodeNumber: Long,
    val title: String,
)