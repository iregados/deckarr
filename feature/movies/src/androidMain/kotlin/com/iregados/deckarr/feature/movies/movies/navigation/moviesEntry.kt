package com.iregados.deckarr.feature.movies.movies.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.movies.movies.MoviesScreen
import com.iregados.deckarr.feature.movies.movies.MoviesViewModel
import com.iregados.deckarr.feature.movies.movies.navigation.keys.MoviesKey

fun EntryProviderScope<NavKey>.moviesEntry(
    moviesViewModel: MoviesViewModel,
    navigateMainTo: (NavKey) -> Unit
) {
    entry<MoviesKey> {
        MoviesScreen(
            moviesViewModel = moviesViewModel,
            navigateMainTo = navigateMainTo
        )
    }
}
