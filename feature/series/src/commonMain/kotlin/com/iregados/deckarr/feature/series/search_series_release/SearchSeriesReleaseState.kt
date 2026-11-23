package com.iregados.deckarr.feature.series.search_series_release

import com.iregados.deckarr.core.util.dto.ReleaseSortOption

data class SearchSeriesReleaseState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val sonarrReleases: List<com.iregados.deckarr.api.sonarr.dto.SonarrRelease>? = null,
    val grabbedReleasesIds: List<String> = emptyList(),
    val errorWhileGrabbingReleasesIds: List<String> = emptyList(),
    val grabbingReleasesIds: List<String> = emptyList(),
    val sortOption: ReleaseSortOption = ReleaseSortOption.APPROVED,
    val sortAscending: Boolean = ReleaseSortOption.APPROVED.defaultAscending,
)

