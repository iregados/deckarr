package com.iregados.deckarr.feature.navigation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.iregados.deckarr.feature.downloads.DownloadsViewModel
import com.iregados.deckarr.feature.movies.movie_detail.navigation.movieDetailEntry
import com.iregados.deckarr.feature.movies.movies.MoviesViewModel
import com.iregados.deckarr.feature.movies.movies_list.navigation.moviesListEntry
import com.iregados.deckarr.feature.movies.search_movie_release.navigation.searchMovieReleaseEntry
import com.iregados.deckarr.feature.navigation.NavigationViewModel
import com.iregados.deckarr.feature.navigation.home.homeNavEntry
import com.iregados.deckarr.feature.search.navigation.searchEntry
import com.iregados.deckarr.feature.series.search_series_release.navigation.searchSeriesReleaseEntry
import com.iregados.deckarr.feature.series.series.SeriesViewModel
import com.iregados.deckarr.feature.series.series_detail.navigation.seriesDetailEntry
import com.iregados.deckarr.feature.series.series_list.navigation.seriesListEntry
import com.iregados.deckarr.feature.settings.navigation.settingsEntry
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainNavigationRoot(
    navViewModel: NavigationViewModel = koinViewModel(),
    hideSplashScreen: () -> Unit
) {
    val moviesViewModel: MoviesViewModel = koinViewModel()
    val seriesViewModel: SeriesViewModel = koinViewModel()
    val downloadsViewModel: DownloadsViewModel = koinViewModel()

    val moviesState by moviesViewModel.uiState.collectAsStateWithLifecycle()
    val seriesState by seriesViewModel.uiState.collectAsStateWithLifecycle()
    val downloadsState by downloadsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(
        moviesState.isLoading,
        seriesState.isLoading,
        downloadsState.isLoading
    ) {
        if (!moviesState.isLoading &&
            !seriesState.isLoading &&
            !downloadsState.isLoading
        ) {
            hideSplashScreen()
        }
    }

    val mainBackStack by navViewModel.mainBackStack.collectAsStateWithLifecycle()

    NavDisplay(
        modifier = Modifier
            .fillMaxSize(),
        backStack = mainBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        onBack = { navViewModel.popMainBackStack() },
        entryProvider = entryProvider {
            homeNavEntry(
                navViewModel = navViewModel,
                moviesViewModel = moviesViewModel,
                seriesViewModel = seriesViewModel,
                downloadsViewModel = downloadsViewModel
            )

            settingsEntry(
                popMainBackStack = { navViewModel.popMainBackStack() }
            )

            searchEntry(
                popMainBackStack = { navViewModel.popMainBackStack() },
                navigateMainTo = { navViewModel.navigateMainTo(it) }
            )

            seriesDetailEntry(
                seriesViewModel = seriesViewModel,
                popMainBackStack = { navViewModel.popMainBackStack() },
                navigateMainTo = { navViewModel.navigateMainTo(it) }
            )

            movieDetailEntry(
                moviesViewModel = moviesViewModel,
                popMainBackStack = { navViewModel.popMainBackStack() },
                navigateMainTo = { navViewModel.navigateMainTo(it) }
            )

            searchMovieReleaseEntry(
                popMainBackStack = { navViewModel.popMainBackStack() }
            )
            searchSeriesReleaseEntry(
                popMainBackStack = { navViewModel.popMainBackStack() }
            )

            moviesListEntry(
                popMainBackStack = { navViewModel.popMainBackStack() },
                navigateMainTo = { navViewModel.navigateMainTo(it) }
            )
            seriesListEntry(
                popMainBackStack = { navViewModel.popMainBackStack() },
                navigateMainTo = { navViewModel.navigateMainTo(it) }
            )
        }
    )
}