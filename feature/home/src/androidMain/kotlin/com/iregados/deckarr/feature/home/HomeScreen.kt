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
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.core.components.MovieCard
import com.iregados.deckarr.core.components.MovieListHeader
import com.iregados.deckarr.feature.home.components.StatsComponent
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    onMoviesListClick: (movies: List<Movie>, title: String) -> Unit,
    homeViewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val radarr = uiState.radarr
    val sonarr = uiState.sonarr
    val downloads = uiState.downloads

    val isAnyLoading = radarr.isLoading || sonarr.isLoading || downloads.isLoading
    val hasAnyData = radarr.movies != null || sonarr.series != null || downloads.torrents != null

    if (isAnyLoading && !hasAnyData) {
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
        radarr.needsConfiguration &&
        sonarr.needsConfiguration &&
        downloads.needsConfiguration
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

    if (!isAnyLoading && !hasAnyData) {
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
        if (isAnyLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        if (radarr.needsConfiguration) {
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

        if (sonarr.needsConfiguration) {
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

        if (downloads.needsConfiguration) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Please set up your Torrent server",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        StatsComponent(
            radarrState = radarr,
            sonarrState = sonarr,
            downloadsState = downloads,
        )

        if (!radarr.needsConfiguration) {
            MovieListHeader(
                title = "Popular movies",
                onClick = { title ->
                    onMoviesListClick(
                        radarr.popularMovies ?: emptyList(),
                        title
                    )
                }
            )

            radarr.popularMovies?.let { popularMovies ->
                LazyHorizontalGrid(
                    modifier = Modifier
                        .height(220.dp),
                    rows = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        popularMovies.size,
                        key = { index -> popularMovies[index].tmdbId }
                    ) { index ->
                        MovieCard(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(190.dp),
                            movie = popularMovies[index],
                            onClick = { onMovieClick(popularMovies[index]) }
                        )
                    }
                }
            }

            MovieListHeader(
                title = "Trending movies",
                onClick = { title ->
                    onMoviesListClick(
                        radarr.trendingMovies ?: emptyList(),
                        title
                    )
                }
            )

            radarr.trendingMovies?.let { trendingMovies ->
                LazyHorizontalGrid(
                    modifier = Modifier
                        .height(220.dp),
                    rows = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        trendingMovies.size,
                        key = { index -> trendingMovies[index].tmdbId }
                    ) { index ->
                        MovieCard(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(190.dp),
                            movie = trendingMovies[index],
                            onClick = { onMovieClick(trendingMovies[index]) }
                        )
                    }
                }
            }
        }
    }
}