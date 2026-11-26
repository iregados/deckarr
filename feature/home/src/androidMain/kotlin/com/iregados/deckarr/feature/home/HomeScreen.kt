package com.iregados.deckarr.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Tv
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
import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.feature.home.components.HomeCategoryHeader
import com.iregados.deckarr.feature.home.components.HomeMoviesSection
import com.iregados.deckarr.feature.home.components.HomeSeriesSection
import com.iregados.deckarr.feature.home.components.StatsComponent
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    onMoviesListClick: (movies: List<Movie>, title: String) -> Unit,
    onSeriesClick: (Series) -> Unit,
    onSeriesListClick: (series: List<Series>, title: String) -> Unit,
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
            HomeCategoryHeader(
                title = "Movies",
                icon = Icons.Outlined.Movie,
            )

            HomeMoviesSection(
                title = "Popular movies",
                emptyMessage = "No popular movies available",
                movies = radarr.popularMovies.orEmpty(),
                isLoading = radarr.isLoading,
                onMovieClick = onMovieClick,
                onMoviesListClick = onMoviesListClick,
            )

            HomeMoviesSection(
                title = "Trending movies",
                emptyMessage = "No trending movies available",
                movies = radarr.trendingMovies.orEmpty(),
                isLoading = radarr.isLoading,
                onMovieClick = onMovieClick,
                onMoviesListClick = onMoviesListClick,
            )
        }

        if (!sonarr.needsConfiguration) {
            HomeCategoryHeader(
                title = "Series",
                icon = Icons.Outlined.Tv,
            )

            HomeSeriesSection(
                title = "Popular series",
                emptyMessage = "No popular series available",
                series = sonarr.popularSeries.orEmpty(),
                isLoading = sonarr.isLoading,
                onSeriesClick = onSeriesClick,
                onSeriesListClick = onSeriesListClick,
            )

            HomeSeriesSection(
                title = "Trending series",
                emptyMessage = "No trending series available",
                series = sonarr.trendingSeries.orEmpty(),
                isLoading = sonarr.isLoading,
                onSeriesClick = onSeriesClick,
                onSeriesListClick = onSeriesListClick,
            )
        }
    }
}