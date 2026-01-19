package com.iregados.deckarr.feature.series.search_series_release.navigation.keys

import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.series.search_series_release.navigation.keys.util.SearchSeriesReleaseParams
import kotlinx.serialization.Serializable

@Serializable
data class SearchSeriesReleaseKey(
    val params: SearchSeriesReleaseParams
) : NavKey