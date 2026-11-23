package com.iregados.deckarr.feature.movies.movies

import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.api.radarr.dto.RadarrRootFolder

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

fun MoviesState.withMovies(movies: List<Movie>?): MoviesState =
    copy(
        movies = movies,
        missingFileMovies = movies
            ?.filter { it.hasFile != true }
            ?.sortedByDescending { it.added },
        recentlyAddedMovies = movies
            ?.filter { it.hasFile == true }
            ?.sortedByDescending { it.added },
    )
