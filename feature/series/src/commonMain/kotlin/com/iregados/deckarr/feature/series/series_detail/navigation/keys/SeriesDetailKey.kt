package com.iregados.deckarr.feature.series.series_detail.navigation.keys

import androidx.navigation3.runtime.NavKey
import com.iregados.api.sonarr.dto.Series
import kotlinx.serialization.Serializable

@Serializable
data class SeriesDetailKey(
    val series: Series
) : NavKey