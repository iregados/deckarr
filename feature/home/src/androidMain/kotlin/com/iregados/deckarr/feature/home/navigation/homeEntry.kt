package com.iregados.deckarr.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.downloads.DownloadsViewModel
import com.iregados.deckarr.feature.home.HomeScreen
import com.iregados.deckarr.feature.movies.movies.MoviesViewModel
import com.iregados.deckarr.feature.navigation.keys.HomeKey
import com.iregados.deckarr.feature.series.series.SeriesViewModel

fun EntryProviderScope<NavKey>.homeEntry(
    navigateMainTo: (NavKey) -> Unit,
    moviesViewModel: MoviesViewModel,
    seriesViewModel: SeriesViewModel,
    downloadViewModel: DownloadsViewModel
) {
    entry<HomeKey> {
        HomeScreen(
            navigateMainTo = navigateMainTo,
            moviesViewModel = moviesViewModel,
            seriesViewModel = seriesViewModel,
            downloadViewModel = downloadViewModel
        )
    }
}
