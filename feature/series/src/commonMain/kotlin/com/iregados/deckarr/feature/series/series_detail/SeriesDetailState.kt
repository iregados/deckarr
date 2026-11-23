package com.iregados.deckarr.feature.series.series_detail

data class SeriesDetailState(
    val isLoading: Boolean = false,
    val isAdding: Boolean = false,
    val isRemoving: Boolean = false,
    val error: String? = null,
    val series: com.iregados.deckarr.api.sonarr.dto.Series? = null,
    val episodes: List<com.iregados.deckarr.api.sonarr.dto.Episode>? = null,
    val sonarrQualityProfiles: List<com.iregados.deckarr.api.sonarr.dto.SonarrQualityProfile>? = null,
    val sonarrRootFolders: List<com.iregados.deckarr.api.sonarr.dto.SonarrRootFolder>? = null,
    val selectedQualityProfileId: Long? = null,
    val selectedRootFolderPath: String? = null,
)

