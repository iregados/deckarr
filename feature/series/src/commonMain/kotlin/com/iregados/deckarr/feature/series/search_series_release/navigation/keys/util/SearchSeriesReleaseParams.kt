package com.iregados.deckarr.feature.series.search_series_release.navigation.keys.util

import kotlinx.serialization.Serializable

@Serializable
sealed interface SearchSeriesReleaseParams {
    @Serializable
    data class BySeriesAndSeason(
        val seriesId: Long,
        val seasonNumber: Long
    ) : SearchSeriesReleaseParams

    @Serializable
    data class ByEpisode(
        val episodeId: Long
    ) : SearchSeriesReleaseParams
}