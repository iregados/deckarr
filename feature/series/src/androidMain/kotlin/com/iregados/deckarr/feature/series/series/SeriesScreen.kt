package com.iregados.deckarr.feature.series.series

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.core.components.EmptyListPlaceholder
import com.iregados.deckarr.core.components.SeriesCard
import com.iregados.deckarr.core.components.SeriesListHeader
import com.iregados.deckarr.feature.series.series_detail.navigation.keys.SeriesDetailKey
import com.iregados.deckarr.feature.series.series_list.navigation.keys.SeriesListKey
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SeriesScreen(
    navigateMainTo: (NavKey) -> Unit,
    seriesViewModel: SeriesViewModel = koinViewModel(),
) {
    val uiState by seriesViewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
        return
    }

    if (uiState.needsConfiguration) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Please set up your Sonarr server",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    if (uiState.series == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = uiState.error ?: "Error while loading series",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
    ) {
        val missingFileSeries = uiState.missingFileSeries.orEmpty()
        if (missingFileSeries.isNotEmpty()) {
            SeriesListHeader(
                title = "Missing file",
                onClick = { title ->
                    navigateMainTo(
                        SeriesListKey(
                            series = missingFileSeries,
                            title = title
                        )
                    )
                }
            )

            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    missingFileSeries.size,
                    key = { index -> missingFileSeries[index].tvdbId }
                ) { index ->
                    SeriesCard(
                        modifier = Modifier
                            .height(120.dp)
                            .width(200.dp),
                        series = missingFileSeries[index],
                        onClick = {
                            navigateMainTo(
                                SeriesDetailKey(
                                    missingFileSeries[index]
                                )
                            )
                        }
                    )
                }
            }
        }

        val recentlyAddedSeries = uiState.recentlyAddedSeries.orEmpty()
        SeriesListHeader(
            title = "Recently added",
            enabled = recentlyAddedSeries.isNotEmpty(),
            onClick = { title ->
                navigateMainTo(
                    SeriesListKey(
                        series = recentlyAddedSeries,
                        title = title
                    )
                )
            }
        )

        if (recentlyAddedSeries.isEmpty()) {
            EmptyListPlaceholder(
                message = "No recently added series"
            )
        } else {
            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    recentlyAddedSeries.size,
                    key = { index -> recentlyAddedSeries[index].tvdbId }
                ) { index ->
                    SeriesCard(
                        modifier = Modifier
                            .height(120.dp)
                            .width(200.dp),
                        series = recentlyAddedSeries[index],
                        onClick = {
                            navigateMainTo(
                                SeriesDetailKey(
                                    recentlyAddedSeries[index]
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}