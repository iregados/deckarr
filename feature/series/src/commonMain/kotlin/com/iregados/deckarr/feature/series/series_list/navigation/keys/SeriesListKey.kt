package com.iregados.deckarr.feature.series.series_list.navigation.keys

import androidx.navigation3.runtime.NavKey
import com.iregados.api.sonarr.dto.Series
import kotlinx.serialization.Serializable

@Serializable
data class SeriesListKey(
    val series: List<Series>,
    val title: String
) : NavKey