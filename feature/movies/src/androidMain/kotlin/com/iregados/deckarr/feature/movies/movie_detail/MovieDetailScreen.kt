package com.iregados.deckarr.feature.movies.movie_detail

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
import androidx.compose.material.icons.filled.Search
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
import com.iregados.api.radarr.dto.Movie
import com.iregados.deckarr.core.presentation.components.SmartSnackBar
import com.iregados.deckarr.core.util.extension.toFormatedSize
import com.iregados.deckarr.core.util.extension.toFormattedDate
import com.iregados.deckarr.core.util.extension.toHourAndMinute
import com.iregados.deckarr.feature.movies.movie_detail.components.AddMovieDialog
import com.iregados.deckarr.feature.movies.movie_detail.components.MovieDetailHeader
import com.iregados.deckarr.feature.movies.movie_detail.components.RemoveMovieDialog
import com.iregados.deckarr.feature.movies.movies.MoviesViewModel
import com.iregados.deckarr.feature.movies.search_movie_release.navigation.keys.SearchMovieReleaseKey
import deckarr.feature.movies.generated.resources.Res
import deckarr.feature.movies.generated.resources.imdb_mono
import deckarr.feature.movies.generated.resources.youtube
import org.jetbrains.compose.resources.vectorResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailScreen(
    moviesViewModel: MoviesViewModel,
    passedMovie: Movie,
    movieDetailViewModel: MovieDetailViewModel = koinViewModel(
        parameters = { parametersOf(passedMovie) }
    ),
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit
) {
    val uiState by movieDetailViewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .width(80.dp)
                                .height(48.dp),
                            onClick = {
                                Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.youtube.com/watch?v=${movie.youTubeTrailerId}".toUri()
                                ).apply {
                                    context.startActivity(this)
                                }
                            }
                        ) {
                            Icon(
                                vectorResource(Res.drawable.youtube),
                                null
                            )
                        }

                        OutlinedButton(
                            modifier = Modifier
                                .width(80.dp)
                                .height(48.dp),
                            enabled = movie.imdbId != null,
                            onClick = {
                                Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.imdb.com/title/${movie.imdbId}".toUri()
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

                        if (movie.id != null) {
                            OutlinedButton(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(48.dp),
                                onClick = {
                                    navigateMainTo(SearchMovieReleaseKey(uiState.movie!!))
                                }
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    null
                                )
                            }

                            OutlinedButton(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(48.dp),
                                onClick = {
                                    showRemoveMovieDialog = true
                                }
                            ) {
                                if (uiState.isRemoving) {
                                    CircularProgressIndicator()
                                } else {
                                    Icon(
                                        Icons.Default.Delete,
                                        "Delete movie",
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
                                    showAddNewMovieDialog = true
                                }
                            ) {
                                if (uiState.isAdding) {
                                    CircularProgressIndicator()
                                } else {
                                    Icon(
                                        Icons.Default.Add,
                                        "Add movie",
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

                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (movie.id != null) {
                                Text(
                                    "Movie is added to Radarr",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            "Added",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            movie.added.toFormattedDate(),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }

                                movie.movieFile?.let {
                                    Text(
                                        "Movie file available",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            it.path,
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    "Added",
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Text(
                                                    it.dateAdded.toFormattedDate(),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }

                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    "Size on disk",
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Text(
                                                    it.size.toFormatedSize(),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }

                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    "Group",
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Text(
                                                    it.releaseGroup ?: "Unknown",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                } ?: run {
                                    Text(
                                        "Movie file is not available",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else {
                                Text(
                                    "Movie is not added to Radarr",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

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
            movieDetailViewModel.addMovie(
                qualityProfileId, rootFolderPath, shouldMonitor
            ) { movie ->
                if (movie != null) {
                    moviesViewModel.addMovieToState(movie)
                }
            }
        },
        onDismiss = { showAddNewMovieDialog = false }
    )

    RemoveMovieDialog(
        showDialog = showRemoveMovieDialog,
        onRemoveMovie = { addToExclusionList, deleteFiles ->
            movieDetailViewModel.removeMovie(
                addToExclusionList, deleteFiles
            ) { movieId ->
                moviesViewModel.removeMovieFromState(movieId)
            }
        },
        onDismiss = { showRemoveMovieDialog = false }
    )

}