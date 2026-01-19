package com.iregados.deckarr.feature.movies.search_movie_release

import com.iregados.api.radarr.dto.RadarrRelease

data class SearchMovieReleaseState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val radarrReleases: List<RadarrRelease>? = null,
    val grabbedReleasesIds: List<String> = emptyList(),
    val errorWhileGrabbingReleasesIds: List<String> = emptyList(),
    val grabbingReleasesIds: List<String> = emptyList()
)

