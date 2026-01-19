package com.iregados.deckarr.feature.series.series

import com.iregados.api.sonarr.dto.Series
import com.iregados.api.sonarr.dto.SonarrRootFolder

data class SeriesState(
    val isLoading: Boolean = true,
    val needsConfiguration: Boolean = false,
    val error: String? = null,
    val series: List<Series>? = null,
    val missingFileSeries: List<Series>? = null,
    val recentlyAddedSeries: List<Series>? = null,
    val rootFolders: List<SonarrRootFolder>? = null
)

