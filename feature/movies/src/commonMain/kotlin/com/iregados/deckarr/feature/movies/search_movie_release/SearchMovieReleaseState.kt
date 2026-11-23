package com.iregados.deckarr.feature.movies.search_movie_release

import com.iregados.deckarr.api.radarr.dto.RadarrRelease
import com.iregados.deckarr.core.util.dto.ReleaseSortOption

data class SearchMovieReleaseState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val radarrReleases: List<RadarrRelease>? = null,
    val grabbedReleasesIds: List<String> = emptyList(),
    val errorWhileGrabbingReleasesIds: List<String> = emptyList(),
    val grabbingReleasesIds: List<String> = emptyList(),
    val sortOption: ReleaseSortOption = ReleaseSortOption.APPROVED,
    val sortAscending: Boolean = ReleaseSortOption.APPROVED.defaultAscending,
)

