package com.iregados.deckarr.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.core.components.EmptyListPlaceholder
import com.iregados.deckarr.core.components.SeriesCard
import com.iregados.deckarr.core.components.SeriesListHeader

@Composable
fun HomeSeriesSection(
    title: String,
    emptyMessage: String,
    series: List<Series>,
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

    if (series.isEmpty()) {
        EmptyListPlaceholder(
            message = emptyMessage
        )
    } else {
        LazyHorizontalGrid(
            modifier = Modifier
                .height(220.dp),
            rows = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                        .fillMaxHeight()
                        .width(190.dp),
                    series = series[index],
                    onClick = { onSeriesClick(series[index]) }
                )
            }
        }
    }
}