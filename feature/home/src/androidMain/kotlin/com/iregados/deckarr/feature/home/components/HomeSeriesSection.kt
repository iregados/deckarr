package com.iregados.deckarr.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.core.components.EmptyListPlaceholder
import com.iregados.deckarr.core.components.MediaListSkeleton
import com.iregados.deckarr.core.components.SeriesCard
import com.iregados.deckarr.core.components.SeriesListHeader

@Composable
fun HomeSeriesSection(
    title: String,
    emptyMessage: String,
    series: List<Series>,
    isLoading: Boolean,
    onSeriesClick: (Series) -> Unit,
    onSeriesListClick: (series: List<Series>, title: String) -> Unit,
) {
    SeriesListHeader(
        title = title,
        enabled = series.isNotEmpty(),
        onClick = { headerTitle ->
            onSeriesListClick(
                series,
                headerTitle
            )
        }
    )

    when {
        isLoading && series.isEmpty() -> MediaListSkeleton()
        series.isEmpty() -> EmptyListPlaceholder(
            message = emptyMessage,
            icon = Icons.Outlined.Tv
        )

        else -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    series.size,
                    key = { index ->
                        val item = series[index]
                        item.tvdbId.takeIf { it != 0L } ?: -item.tmdbId
                    }
                ) { index ->
                    SeriesCard(
                        modifier = Modifier
                            .height(120.dp)
                            .width(200.dp),
                        series = series[index],
                        onClick = { onSeriesClick(series[index]) }
                    )
                }
            }
        }
    }
}