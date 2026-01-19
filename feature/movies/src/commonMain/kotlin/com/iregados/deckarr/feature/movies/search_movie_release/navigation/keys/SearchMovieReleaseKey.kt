package com.iregados.deckarr.feature.movies.search_movie_release.navigation.keys

import androidx.navigation3.runtime.NavKey
import com.iregados.api.radarr.dto.Movie
import kotlinx.serialization.Serializable

@Serializable
data class SearchMovieReleaseKey(
    val movie: Movie
) : NavKey