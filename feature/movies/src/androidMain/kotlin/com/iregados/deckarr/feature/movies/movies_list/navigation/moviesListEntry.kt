package com.iregados.deckarr.feature.movies.movies_list.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.movies.movies_list.MoviesList

fun EntryProviderScope<NavKey>.moviesListEntry(
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit
) {
    entry<com.iregados.deckarr.feature.movies.keys.MoviesListKey> { key ->
        MoviesList(
            passedMovies = key.movies,
            title = key.title,
            popMainBackStack = popMainBackStack,
            navigateMainTo = navigateMainTo
        )
    }
}
