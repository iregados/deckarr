package com.iregados.api.radarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class RadarrRelease(
    val guid: String,
    val quality: Quality,
    val qualityWeight: Long,
    val age: Long,
    val ageHours: Double,
    val ageMinutes: Double,
    val size: Long,
    val indexerId: Long,
    val indexer: String,
    val releaseGroup: String?,
    val title: String,
    val sceneSource: Boolean,
    val movieTitles: List<String>,
    val languages: List<Language>,
    val mappedMovieId: Long?,
    val approved: Boolean,
    val rejected: Boolean,
    val tmdbId: Long,
    val imdbId: Long,
    val rejections: List<String>,
    val publishDate: String,
    val commentUrl: String,
    val downloadUrl: String,
    val infoUrl: String,
    val downloadAllowed: Boolean,
    val magnetUrl: String,
    val seeders: Long,
    val leechers: Long,
    val protocol: String,
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
    val modifier: String,
)

@Serializable
data class Revision(
    val version: Long,
    val real: Long,
    val isRepack: Boolean,
)