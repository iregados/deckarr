package com.iregados.deckarr.feature.movies.movie_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.core.components.SmartSnackBar
import com.iregados.deckarr.core.util.extension.toFormattedDate
import com.iregados.deckarr.core.util.extension.toHourAndMinute
import com.iregados.deckarr.feature.movies.movie_detail.components.AddMovieDialog
import com.iregados.deckarr.feature.movies.movie_detail.components.MovieDetailActionsRow
import com.iregados.deckarr.feature.movies.movie_detail.components.MovieDetailHeader
import com.iregados.deckarr.feature.movies.movie_detail.components.MovieFileCard
import com.iregados.deckarr.feature.movies.movie_detail.components.RemoveMovieDialog
import com.iregados.deckarr.feature.movies.search_movie_release.navigation.keys.SearchMovieReleaseKey
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailScreen(
    passedMovie: Movie,
    movieDetailViewModel: MovieDetailViewModel = koinViewModel(
        parameters = { parametersOf(passedMovie) }
    ),
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit
) {
    val uiState by movieDetailViewModel.uiState.collectAsStateWithLifecycle()

    var showAddNewMovieDialog by remember { mutableStateOf(false) }
    var showRemoveMovieDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SmartSnackBar(movieDetailViewModel.notificationFlow) }
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

        if (uiState.movie == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error while loading movie",
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


            uiState.movie?.let { movie ->
                MovieDetailHeader(
                    modifier = Modifier,
                    movie = movie,
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
                            movie.year.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            movie.genres.take(2).joinToString(", "),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            movie.runtime.toHourAndMinute(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    MovieDetailActionsRow(
                        movie = movie,
                        isAdding = uiState.isAdding,
                        isRemoving = uiState.isRemoving,
                        onSearchReleaseClick = {
                            navigateMainTo(SearchMovieReleaseKey(movie))
                        },
                        onAddClick = { showAddNewMovieDialog = true },
                        onRemoveClick = { showRemoveMovieDialog = true },
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Cinema release",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                movie.inCinemas.toFormattedDate(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Digital release",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                movie.digitalRelease.toFormattedDate(),
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
                                "${movie.ratings?.imdb?.value ?: "Unknown"}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    MovieFileCard(movie = movie)

                    Text(
                        movie.overview,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    AddMovieDialog(
        showDialog = showAddNewMovieDialog,
        uiState = uiState,
        onAddMovie = { qualityProfileId, rootFolderPath, shouldMonitor ->
            showAddNewMovieDialog = false
            movieDetailViewModel.onEvent(
                MovieDetailEvent.AddMovie(
                    qualityProfileId = qualityProfileId,
                    rootFolderPath = rootFolderPath,
                    shouldMonitor = shouldMonitor,
                )
            )
        },
        onDismiss = { showAddNewMovieDialog = false }
    )

    RemoveMovieDialog(
        showDialog = showRemoveMovieDialog,
        onRemoveMovie = { addToExclusionList, deleteFiles ->
            movieDetailViewModel.onEvent(
                MovieDetailEvent.RemoveMovie(
                    addToExclusionList = addToExclusionList,
                    deleteFiles = deleteFiles,
                )
            )
        },
        onDismiss = { showRemoveMovieDialog = false }
    )
}