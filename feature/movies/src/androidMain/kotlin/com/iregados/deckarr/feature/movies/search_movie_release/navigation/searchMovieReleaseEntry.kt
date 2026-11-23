package com.iregados.deckarr.feature.movies.search_movie_release.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.movies.search_movie_release.SearchMovieRelease

fun EntryProviderScope<NavKey>.searchMovieReleaseEntry(
    popMainBackStack: () -> Unit,
) {
    entry<com.iregados.deckarr.feature.movies.search_movie_release.navigation.keys.SearchMovieReleaseKey> { key ->
        SearchMovieRelease(
            passedMovie = key.movie,
            popMainBackStack = popMainBackStack
        )
    }
}
