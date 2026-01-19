package com.iregados.deckarr.feature.movies.movies

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
import com.iregados.deckarr.core.presentation.components.MovieCard
import com.iregados.deckarr.core.presentation.components.MovieListHeader
import com.iregados.deckarr.feature.movies.movie_detail.navigation.keys.MovieDetailKey
import com.iregados.deckarr.feature.movies.movies_list.navigation.keys.MoviesListKey


@Composable
fun MoviesScreen(
    moviesViewModel: MoviesViewModel,
    navigateMainTo: (NavKey) -> Unit
) {
    val uiState by moviesViewModel.uiState.collectAsStateWithLifecycle()

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
                    .padding(bottom = 16.dp)
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
                text = "Please set up your Radarr server",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    if (uiState.movies == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error while loading movies",
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
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        MovieListHeader(
            title = "Missing file",
            onClick = { title ->
                navigateMainTo(
                    MoviesListKey(
                        movies = uiState.missingFileMovies
                            ?: emptyList(),
                        title = title
                    )
                )
            }
        )

        uiState.missingFileMovies?.let { missingFileMovies ->
            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    missingFileMovies.size,
                    key = { index -> missingFileMovies[index].tmdbId }
                ) { index ->
                    MovieCard(
                        modifier = Modifier
                            .height(120.dp)
                            .width(200.dp),
                        movie = missingFileMovies[index],
                        onClick = {
                            navigateMainTo(
                                MovieDetailKey(
                                    missingFileMovies[index]
                                )
                            )
                        }
                    )
                }
            }
        }

        MovieListHeader(
            title = "Recently added",
            onClick = { title ->
                navigateMainTo(
                    MoviesListKey(
                        movies = uiState.recentlyAddedMovies ?: emptyList(),
                        title = title
                    )
                )
            }
        )


        uiState.recentlyAddedMovies?.let { recentlyDownloadedMovies ->
            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    recentlyDownloadedMovies.size, key = { index ->
                        recentlyDownloadedMovies[index].tmdbId
                    }
                ) { index ->
                    MovieCard(
                        modifier = Modifier
                            .height(120.dp)
                            .width(200.dp),
                        movie = recentlyDownloadedMovies[index],
                        onClick = {
                            navigateMainTo(
                                MovieDetailKey(
                                    recentlyDownloadedMovies[index]
                                )
                            )
                        }
                    )
                }
            }
        }


        MovieListHeader(
            title = "Recommended",
            onClick = { title ->
                navigateMainTo(
                    MoviesListKey(
                        movies = uiState.recommendedMovies ?: emptyList(),
                        title = title
                    )
                )
            }
        )

        uiState.recommendedMovies?.let { recommendedMovies ->
            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    recommendedMovies.size,
                    key = { index -> recommendedMovies[index].tmdbId }
                ) { index ->
                    MovieCard(
                        modifier = Modifier
                            .height(120.dp)
                            .width(200.dp),
                        movie = recommendedMovies[index],
                        onClick = {
                            navigateMainTo(
                                MovieDetailKey(
                                    recommendedMovies[index]
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}