package com.iregados.deckarr.feature.series.series_detail

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.iregados.api.sonarr.dto.Series
import com.iregados.deckarr.core.presentation.components.SmartSnackBar
import com.iregados.deckarr.core.util.extension.toFormattedDate
import com.iregados.deckarr.core.util.extension.toHourAndMinute
import com.iregados.deckarr.feature.series.series.SeriesViewModel
import com.iregados.deckarr.feature.series.series_detail.components.AddSeriesDialog
import com.iregados.deckarr.feature.series.series_detail.components.EpisodeBySeasonList
import com.iregados.deckarr.feature.series.series_detail.components.RemoveSeriesDialog
import com.iregados.deckarr.feature.series.series_detail.components.SeriesDetailHeader
import deckarr.feature.series.generated.resources.Res
import deckarr.feature.series.generated.resources.imdb_mono
import org.jetbrains.compose.resources.vectorResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeriesDetailScreen(
    seriesViewModel: SeriesViewModel,
    passedSeries: Series,
    seriesDetailViewModel: SeriesDetailViewModel = koinViewModel(
        parameters = { parametersOf(passedSeries) }
    ),
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit,
) {
    val uiState by seriesDetailViewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
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
                    text = "Error while loading series",
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .width(80.dp)
                                .height(48.dp),
                            enabled = series.imdbId != null,
                            onClick = {
                                Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.imdb.com/title/${series.imdbId}".toUri()
                                ).apply {
                                    context.startActivity(this)
                                }
                            }
                        ) {
                            Icon(
                                vectorResource(Res.drawable.imdb_mono),
                                null
                            )
                        }

                        if (series.id != null) {
                            OutlinedButton(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(48.dp),
                                onClick = {
                                    showRemoveSeriesDialog = true
                                }
                            ) {
                                if (uiState.isRemoving) {
                                    CircularProgressIndicator()
                                } else {
                                    Icon(
                                        Icons.Default.Delete,
                                        null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        } else {
                            OutlinedButton(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(48.dp),
                                onClick = {
                                    showAddNewSeriesDialog = true
                                }
                            ) {
                                if (uiState.isAdding) {
                                    CircularProgressIndicator()
                                } else {
                                    Icon(
                                        Icons.Default.Add,
                                        null
                                    )
                                }
                            }
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
            seriesDetailViewModel.addSeries(
                qualityProfileId, rootFolderPath, shouldMonitor
            ) { series ->
                if (series != null) {
                    seriesViewModel.addSeriesToState(series)
                }
            }
        },
        onDismiss = { showAddNewSeriesDialog = false }
    )

    RemoveSeriesDialog(
        showDialog = showRemoveSeriesDialog,
        onRemoveSeries = { addToExclusionList, deleteFiles ->
            showRemoveSeriesDialog = false
            seriesDetailViewModel.removeSeries(
                addToExclusionList, deleteFiles
            ) { seriesId ->
                seriesViewModel.removeSeriesFromState(seriesId)
            }
        },
        onDismiss = { showRemoveSeriesDialog = false }
    )

}