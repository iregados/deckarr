package com.iregados.api.radarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieFile(
    val id: Long,
    val movieId: Long,
    val relativePath: String,
    val path: String,
    val size: Long,
    val dateAdded: String,
    val sceneName: String?,
    val releaseGroup: String?,
    val mediaInfo: MediaInfo?,
    val originalFilePath: String?,
)