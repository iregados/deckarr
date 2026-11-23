package com.iregados.deckarr.feature.movies.keys

import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.api.radarr.dto.Movie
import kotlinx.serialization.Serializable

@Serializable
data class MoviesListKey(
    val movies: List<Movie>,
    val title: String
) : NavKey