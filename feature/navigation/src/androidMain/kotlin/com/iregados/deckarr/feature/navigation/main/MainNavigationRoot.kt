package com.iregados.deckarr.feature.navigation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.iregados.deckarr.feature.downloads.navigation.downloadsEntry
import com.iregados.deckarr.feature.home.navigation.homeEntry
import com.iregados.deckarr.feature.movies.keys.MoviesListKey
import com.iregados.deckarr.feature.movies.movie_detail.navigation.keys.MovieDetailKey
import com.iregados.deckarr.feature.movies.movie_detail.navigation.movieDetailEntry
import com.iregados.deckarr.feature.movies.movies.navigation.moviesEntry
import com.iregados.deckarr.feature.movies.movies_list.navigation.moviesListEntry
import com.iregados.deckarr.feature.movies.search_movie_release.navigation.searchMovieReleaseEntry
import com.iregados.deckarr.feature.navigation.BottomNavTab
import com.iregados.deckarr.feature.navigation.HomeShellViewModel
import com.iregados.deckarr.feature.navigation.home.rememberHomeNavigationSceneDecoratorStrategy
import com.iregados.deckarr.feature.navigation.home.topLevelChrome
import com.iregados.deckarr.feature.navigation.rememberNavigationState
import com.iregados.deckarr.feature.navigation.rememberNavigator
import com.iregados.deckarr.feature.navigation.toEntries
import com.iregados.deckarr.feature.search.navigation.searchEntry
import com.iregados.deckarr.feature.series.search_series_release.navigation.searchSeriesReleaseEntry
import com.iregados.deckarr.feature.series.series.navigation.seriesEntry
import com.iregados.deckarr.feature.series.series_detail.navigation.keys.SeriesDetailKey
import com.iregados.deckarr.feature.series.series_detail.navigation.seriesDetailEntry
import com.iregados.deckarr.feature.series.series_list.navigation.keys.SeriesListKey
import com.iregados.deckarr.feature.series.series_list.navigation.seriesListEntry
import com.iregados.deckarr.feature.settings.navigation.settingsEntry
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavigationRoot(
    shellViewModel: HomeShellViewModel = koinViewModel(),
) {
    val navigationState = rememberNavigationState()
    val navigator = rememberNavigator(navigationState)

    val entryProvider = remember(navigator) {
        entryProvider {
            homeEntry(
                onMovieClick = { movie -> navigator.navigate(MovieDetailKey(movie)) },
                onMoviesListClick = { movies, title ->
                    navigator.navigate(MoviesListKey(movies = movies, title = title))
                },
                onSeriesClick = { series -> navigator.navigate(SeriesDetailKey(series)) },
                onSeriesListClick = { series, title ->
                    navigator.navigate(SeriesListKey(series = series, title = title))
                },
                metadata = topLevelChrome(BottomNavTab.HOME),
            )
            moviesEntry(
                navigateMainTo = { navigator.navigate(it) },
                metadata = topLevelChrome(BottomNavTab.MOVIES),
            )
            seriesEntry(
                navigateMainTo = { navigator.navigate(it) },
                metadata = topLevelChrome(BottomNavTab.SERIES),
            )
            downloadsEntry(
                metadata = topLevelChrome(BottomNavTab.DOWNLOADS),
            )

            settingsEntry(popMainBackStack = { navigator.goBack() })

            searchEntry(
                popMainBackStack = { navigator.goBack() },
                onMovieClick = { movie -> navigator.navigate(MovieDetailKey(movie)) },
                onSeriesClick = { series -> navigator.navigate(SeriesDetailKey(series)) },
            )

            seriesDetailEntry(
                popMainBackStack = { navigator.goBack() },
                navigateMainTo = { navigator.navigate(it) },
            )

            movieDetailEntry(
                popMainBackStack = { navigator.goBack() },
                navigateMainTo = { navigator.navigate(it) },
            )

            searchMovieReleaseEntry(popMainBackStack = { navigator.goBack() })

            searchSeriesReleaseEntry(popMainBackStack = { navigator.goBack() })

            moviesListEntry(
                popMainBackStack = { navigator.goBack() },
                navigateMainTo = { navigator.navigate(it) },
            )

            seriesListEntry(
                popMainBackStack = { navigator.goBack() },
                navigateMainTo = { navigator.navigate(it) },
            )
        }
    }

    val homeSceneDecorator = rememberHomeNavigationSceneDecoratorStrategy(
        onTabSelect = { navigator.selectTab(it) },
        onNavigate = { navigator.navigate(it) },
        notificationFlow = shellViewModel.notificationFlow,
        onRefresh = { tab -> shellViewModel.refresh(tab) },
    )

    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        transitionSpec = { fadeIn() togetherWith ExitTransition.KeepUntilTransitionsFinished },
        popTransitionSpec = { EnterTransition.None togetherWith fadeOut() },
        predictivePopTransitionSpec = { EnterTransition.None togetherWith fadeOut() },
        sceneDecoratorStrategies = listOf(homeSceneDecorator),
    )
}