package com.iregados.api.radarr.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val title: String,
    val originalTitle: String?,
    val sizeOnDisk: Long?,
    val status: String,
    val overview: String,
    val inCinemas: String?,
    val physicalRelease: String?,
    val digitalRelease: String?,
    val releaseDate: String?,
    val images: List<@Contextual RadarrImage>,
    val year: Long,
    val youTubeTrailerId: String,
    val hasFile: Boolean?,
    val movieFileId: Long?,
    val monitored: Boolean?,
    val folderName: String?,
    val runtime: Long,
    val cleanTitle: String?,
    val imdbId: String?,
    val tmdbId: Long,
    val genres: List<String>,
    val added: String?,
    val id: Long?,
    val ratings: Ratings?,
    val movieFile: MovieFile?,
    val isAvailable: Boolean?,
    val isExisting: Boolean?,
    val isTrending: Boolean?,
    val isPopular: Boolean?,
    val isRecommendation: Boolean?,
    val path: String?,
    val rootFolderPath: String?,
    val qualityProfileId: Long?,
    val addOptions: AddOptions?,
    val minimumAvailability: String?
)
