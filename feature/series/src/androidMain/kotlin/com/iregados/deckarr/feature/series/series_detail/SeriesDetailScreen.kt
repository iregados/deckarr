package com.iregados.deckarr.feature.series.series_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.core.components.SmartSnackBar
import com.iregados.deckarr.core.util.extension.toFormattedDate
import com.iregados.deckarr.core.util.extension.toHourAndMinute
import com.iregados.deckarr.feature.series.series_detail.components.AddSeriesDialog
import com.iregados.deckarr.feature.series.series_detail.components.EpisodeBySeasonList
import com.iregados.deckarr.feature.series.series_detail.components.RemoveSeriesDialog
import com.iregados.deckarr.feature.series.series_detail.components.SeriesDetailActionsRow
import com.iregados.deckarr.feature.series.series_detail.components.SeriesDetailHeader
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeriesDetailScreen(
    passedSeries: Series,
    seriesDetailViewModel: SeriesDetailViewModel = koinViewModel(
        parameters = { parametersOf(passedSeries) }
    ),
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit,
) {
    val uiState by seriesDetailViewModel.uiState.collectAsStateWithLifecycle()

    var showAddNewSeriesDialog by remember { mutableStateOf(false) }
    var showRemoveSeriesDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SmartSnackBar(seriesDetailViewModel.notificationFlow) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.TopStart
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            return@Scaffold
        }

        if (uiState.series == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "Error while loading series",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            uiState.series?.let { series ->
                SeriesDetailHeader(
                    modifier = Modifier,
                    series = series,
                    onBackClick = { popMainBackStack() },
                    paddingValues = paddingValues
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            series.year.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            series.genres.take(2).joinToString(", "),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            series.runtime.toHourAndMinute(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    SeriesDetailActionsRow(
                        series = series,
                        isAdding = uiState.isAdding,
                        isRemoving = uiState.isRemoving,
                        onAddClick = { showAddNewSeriesDialog = true },
                        onRemoveClick = { showRemoveSeriesDialog = true },
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Running date",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                series.firstAired.toFormattedDate() + " - " + series.lastAired.toFormattedDate(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "IMDB",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "${if (series.ratings.votes == 0L) "N/A" else series.ratings.value}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Network",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                series.network ?: "N/A",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Status",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                series.status.first().uppercase() + series.status.substring(1),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Text(
                        series.overview ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    uiState.episodes?.let { episodes ->
                        Card {
                            EpisodeBySeasonList(
                                episodes = episodes,
                                navigateMainTo = navigateMainTo
                            )
                        }
                    }
                }
            }
        }
    }

    AddSeriesDialog(
        showDialog = showAddNewSeriesDialog,
        uiState = uiState,
        onAddSeries = { qualityProfileId, rootFolderPath, shouldMonitor ->
            showAddNewSeriesDialog = false
            seriesDetailViewModel.onEvent(
                SeriesDetailEvent.AddSeries(
                    qualityProfileId = qualityProfileId,
                    rootFolderPath = rootFolderPath,
                    shouldMonitor = shouldMonitor,
                )
            )
        },
        onDismiss = { showAddNewSeriesDialog = false }
    )

    RemoveSeriesDialog(
        showDialog = showRemoveSeriesDialog,
        onRemoveSeries = { addToExclusionList, deleteFiles ->
            showRemoveSeriesDialog = false
            seriesDetailViewModel.onEvent(
                SeriesDetailEvent.RemoveSeries(
                    addToExclusionList = addToExclusionList,
                    deleteFiles = deleteFiles,
                )
            )
        },
        onDismiss = { showRemoveSeriesDialog = false }
    )
}