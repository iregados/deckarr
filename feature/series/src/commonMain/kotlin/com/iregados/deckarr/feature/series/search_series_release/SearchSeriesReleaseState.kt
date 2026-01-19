package com.iregados.deckarr.feature.series.search_series_release

import com.iregados.api.sonarr.dto.SonarrRelease

data class SearchSeriesReleaseState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val sonarrReleases: List<SonarrRelease>? = null,
    val grabbedReleasesIds: List<String> = emptyList(),
    val errorWhileGrabbingReleasesIds: List<String> = emptyList(),
    val grabbingReleasesIds: List<String> = emptyList()
)

