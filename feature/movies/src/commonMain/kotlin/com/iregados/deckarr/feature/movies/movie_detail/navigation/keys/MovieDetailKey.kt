package com.iregados.deckarr.feature.movies.movie_detail.navigation.keys

import androidx.navigation3.runtime.NavKey
import com.iregados.api.radarr.dto.Movie
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailKey(
    val movie: Movie
) : NavKey