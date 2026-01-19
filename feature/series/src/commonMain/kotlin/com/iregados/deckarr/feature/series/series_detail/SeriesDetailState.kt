package com.iregados.deckarr.feature.series.series_detail

import com.iregados.api.sonarr.dto.Episode
import com.iregados.api.sonarr.dto.Series
import com.iregados.api.sonarr.dto.SonarrQualityProfile
import com.iregados.api.sonarr.dto.SonarrRootFolder

data class SeriesDetailState(
    val isLoading: Boolean = false,
    val isAdding: Boolean = false,
    val isRemoving: Boolean = false,
    val error: String? = null,
    val series: Series? = null,
    val episodes: List<Episode>? = null,
    val sonarrQualityProfiles: List<SonarrQualityProfile>? = null,
    val sonarrRootFolders: List<SonarrRootFolder>? = null,
    val selectedQualityProfileId: Long? = null,
    val selectedRootFolderPath: String? = null,
)

