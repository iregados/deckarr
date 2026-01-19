package com.iregados.deckarr.feature.navigation.home

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.downloads.DownloadsViewModel
import com.iregados.deckarr.feature.movies.movies.MoviesViewModel
import com.iregados.deckarr.feature.navigation.NavigationViewModel
import com.iregados.deckarr.feature.navigation.keys.BaseKey
import com.iregados.deckarr.feature.series.series.SeriesViewModel

fun EntryProviderScope<NavKey>.homeNavEntry(
    navViewModel: NavigationViewModel,
    moviesViewModel: MoviesViewModel,
    seriesViewModel: SeriesViewModel,
    downloadsViewModel: DownloadsViewModel
) {
    entry<BaseKey> {
        HomeNavigationRoot(
            navViewModel = navViewModel,
            moviesViewModel = moviesViewModel,
            seriesViewModel = seriesViewModel,
            downloadsViewModel = downloadsViewModel
        )
    }
}
