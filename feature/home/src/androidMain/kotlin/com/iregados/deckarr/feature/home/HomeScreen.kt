package com.iregados.deckarr.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
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
import com.iregados.deckarr.feature.downloads.DownloadsViewModel
import com.iregados.deckarr.feature.home.components.StatsComponent
import com.iregados.deckarr.feature.movies.movie_detail.navigation.keys.MovieDetailKey
import com.iregados.deckarr.feature.movies.movies.MoviesViewModel
import com.iregados.deckarr.feature.movies.movies_list.navigation.keys.MoviesListKey
import com.iregados.deckarr.feature.series.series.SeriesViewModel


@Composable
fun HomeScreen(
    navigateMainTo: (NavKey) -> Unit,
    moviesViewModel: MoviesViewModel,
    seriesViewModel: SeriesViewModel,
    downloadViewModel: DownloadsViewModel
) {
    val moviesState by moviesViewModel.uiState.collectAsStateWithLifecycle()
    val seriesState by seriesViewModel.uiState.collectAsStateWithLifecycle()
    val downloadsState by downloadViewModel.uiState.collectAsStateWithLifecycle()

    if (moviesState.isLoading || seriesState.isLoading || downloadsState.isLoading) {
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

    if (
        moviesState.needsConfiguration &&
        seriesState.needsConfiguration &&
        downloadsState.needsConfiguration
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Please set up your servers",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    if (
        moviesState.movies == null &&
        seriesState.series == null &&
        downloadsState.torrents == null
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Connection failed",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
    ) {
        if (moviesState.needsConfiguration) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Please set up your Radarr server",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (seriesState.needsConfiguration) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Please set up your Sonarr server",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (downloadsState.needsConfiguration) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Please set up your Transmission server",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        StatsComponent(
            moviesState = moviesState,
            seriesState = seriesState,
            downloadsState = downloadsState,
        )

        if (!moviesState.needsConfiguration) {
            MovieListHeader(
                title = "Popular movies",
                onClick = { title ->
                    navigateMainTo(
                        MoviesListKey(
                            movies = moviesState.popularMovies ?: emptyList(),
                            title = title
                        )
                    )
                }
            )

            moviesState.popularMovies?.let { popularMovies ->
                LazyHorizontalGrid(
                    modifier = Modifier
                        .height(220.dp),
                    rows = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        popularMovies.size,
                        key = { index -> popularMovies[index].tmdbId }) { index ->
                        MovieCard(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(190.dp),
                            movie = popularMovies[index],
                            onClick = {
                                navigateMainTo(
                                    MovieDetailKey(
                                        popularMovies[index]
                                    )
                                )
                            }
                        )
                    }
                }
            }

            MovieListHeader(
                title = "Trending movies",
                onClick = { title ->
                    navigateMainTo(
                        MoviesListKey(
                            movies = moviesState.trendingMovies ?: emptyList(),
                            title = title
                        )
                    )
                }
            )

            moviesState.trendingMovies?.let { trendingMovies ->
                LazyHorizontalGrid(
                    modifier = Modifier
                        .height(220.dp),
                    rows = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        trendingMovies.size,
                        key = { index -> trendingMovies[index].tmdbId }) { index ->
                        MovieCard(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(190.dp),
                            movie = trendingMovies[index],
                            onClick = {
                                navigateMainTo(
                                    MovieDetailKey(
                                        trendingMovies[index]
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}