package com.iregados.deckarr.core.domain.dto

import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.api.sonarr.dto.SonarrRootFolder

data class SonarrLibraryState(
    val isLoading: Boolean = true,
    val isConfigLoaded: Boolean = false,
    val needsConfiguration: Boolean = false,
    val error: String? = null,
    val series: List<Series>? = null,
    val trendingSeries: List<Series>? = null,
    val popularSeries: List<Series>? = null,
    val rootFolders: List<SonarrRootFolder>? = null,
)