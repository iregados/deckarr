package com.iregados.api.sonarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class Series(
    val id: Long?,
    val title: String,
    val sortTitle: String,
    val status: String,
    val ended: Boolean,
    val overview: String?,
    val previousAiring: String?,
    val network: String?,
    val airTime: String?,
    val images: List<SonarrImage>,
    val originalLanguage: OriginalLanguage,
    val seasons: List<Season>,
    val year: Long,
    val path: String?,
    val qualityProfileId: Long,
    val seasonFolder: Boolean,
    val monitored: Boolean,
    val monitorNewItems: String,
    val useSceneNumbering: Boolean,
    val runtime: Long,
    val tvdbId: Long,
    val tvRageId: Long,
    val tvMazeId: Long,
    val tmdbId: Long,
    val imdbId: String?,
    val firstAired: String?,
    val lastAired: String?,
    val seriesType: String,
    val cleanTitle: String,
    val titleSlug: String,
    val rootFolderPath: String?,
    val certification: String?,
    val genres: List<String>,
    val added: String,
    val ratings: Ratings,
    val statistics: SeriesStatistics,
    val languageProfileId: Long,
    val nextAiring: String?,
    val addOptions: AddOptions?
)

@Serializable
data class OriginalLanguage(
    val id: Long,
    val name: String,
)

@Serializable
data class Ratings(
    val votes: Long,
    val value: Double,
)

@Serializable
data class SeriesStatistics(
    val seasonCount: Long,
    val episodeFileCount: Long,
    val episodeCount: Long,
    val totalEpisodeCount: Long,
    val sizeOnDisk: Long,
    val releaseGroups: List<String>?,
    val percentOfEpisodes: Double,
)
