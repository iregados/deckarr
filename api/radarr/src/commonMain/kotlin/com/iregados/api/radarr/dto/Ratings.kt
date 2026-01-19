package com.iregados.api.radarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class Ratings(
    val imdb: Imdb?,
    val tmdb: Tmdb?,
    val metacritic: Metacritic?,
    val rottenTomatoes: RottenTomatoes?,
    val trakt: Trakt?,
)

@Serializable
data class Imdb(
    val votes: Long,
    val value: Double,
    val type: String,
)

@Serializable
data class Tmdb(
    val votes: Long,
    val value: Double,
    val type: String,
)

@Serializable
data class Metacritic(
    val votes: Long,
    val value: Long,
    val type: String,
)

@Serializable
data class RottenTomatoes(
    val votes: Long,
    val value: Long,
    val type: String,
)

@Serializable
data class Trakt(
    val votes: Long,
    val value: Double,
    val type: String,
)