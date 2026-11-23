package com.iregados.deckarr.core.domain.dto

import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.api.radarr.dto.RadarrRootFolder

data class RadarrLibraryState(
    val isLoading: Boolean = true,
    val isConfigLoaded: Boolean = false,
    val needsConfiguration: Boolean = false,
    val error: String? = null,
    val movies: List<Movie>? = null,
    val recommendedMovies: List<Movie>? = null,
    val trendingMovies: List<Movie>? = null,
    val popularMovies: List<Movie>? = null,
    val rootFolders: List<RadarrRootFolder>? = null,
)