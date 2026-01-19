package com.iregados.deckarr.feature.movies.movies_list.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.movies.movies_list.MoviesList
import com.iregados.deckarr.feature.movies.movies_list.navigation.keys.MoviesListKey

fun EntryProviderScope<NavKey>.moviesListEntry(
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit
) {
    entry<MoviesListKey> { key ->
        MoviesList(
            passedMovies = key.movies,
            title = key.title,
            popMainBackStack = popMainBackStack,
            navigateMainTo = navigateMainTo
        )
    }
}
