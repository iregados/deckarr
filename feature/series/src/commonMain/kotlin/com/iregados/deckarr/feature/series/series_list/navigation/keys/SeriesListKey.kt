package com.iregados.deckarr.feature.series.series_list.navigation.keys

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class SeriesListKey(
    val series: List<com.iregados.deckarr.api.sonarr.dto.Series>,
    val title: String
) : NavKey