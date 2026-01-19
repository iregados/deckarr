package com.iregados.deckarr.feature.movies.movie_detail.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.movies.movie_detail.MovieDetailScreen
import com.iregados.deckarr.feature.movies.movie_detail.navigation.keys.MovieDetailKey
import com.iregados.deckarr.feature.movies.movies.MoviesViewModel

fun EntryProviderScope<NavKey>.movieDetailEntry(
    moviesViewModel: MoviesViewModel,
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit
) {
    entry<MovieDetailKey> { key ->
        MovieDetailScreen(
            moviesViewModel = moviesViewModel,
            passedMovie = key.movie,
            popMainBackStack = popMainBackStack,
            navigateMainTo = navigateMainTo
        )
    }
}
