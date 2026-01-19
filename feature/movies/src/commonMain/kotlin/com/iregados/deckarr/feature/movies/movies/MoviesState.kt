package com.iregados.deckarr.feature.movies.movies

import com.iregados.api.radarr.dto.Movie
import com.iregados.api.radarr.dto.RadarrRootFolder

data class MoviesState(
    val isLoading: Boolean = true,
    val needsConfiguration: Boolean = false,
    val error: String? = null,
    val movies: List<Movie>? = null,
    val recommendedMovies: List<Movie>? = null,
    val trendingMovies: List<Movie>? = null,
    val popularMovies: List<Movie>? = null,
    val missingFileMovies: List<Movie>? = null,
    val recentlyAddedMovies: List<Movie>? = null,
    val rootFolders: List<RadarrRootFolder>? = null
)

