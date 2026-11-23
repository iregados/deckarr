package com.iregados.deckarr.feature.series.series

data class SeriesState(
    val isLoading: Boolean = true,
    val needsConfiguration: Boolean = false,
    val error: String? = null,
    val series: List<com.iregados.deckarr.api.sonarr.dto.Series>? = null,
    val missingFileSeries: List<com.iregados.deckarr.api.sonarr.dto.Series>? = null,
    val recentlyAddedSeries: List<com.iregados.deckarr.api.sonarr.dto.Series>? = null,
    val rootFolders: List<com.iregados.deckarr.api.sonarr.dto.SonarrRootFolder>? = null
)

fun SeriesState.withSeries(series: List<com.iregados.deckarr.api.sonarr.dto.Series>?): SeriesState =
    copy(
        series = series,
        missingFileSeries = series?.filter { it.statistics.episodeFileCount == 0L },
        recentlyAddedSeries = series?.filter { it.statistics.episodeFileCount != 0L },
    )
