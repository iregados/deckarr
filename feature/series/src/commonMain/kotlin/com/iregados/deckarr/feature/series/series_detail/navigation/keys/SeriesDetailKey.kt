package com.iregados.deckarr.feature.series.series_detail.navigation.keys

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class SeriesDetailKey(
    val series: com.iregados.deckarr.api.sonarr.dto.Series
) : NavKey