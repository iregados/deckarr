package com.iregados.deckarr.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.feature.home.HomeScreen
import com.iregados.deckarr.feature.home.navigation.keys.HomeKey

fun EntryProviderScope<NavKey>.homeEntry(
    onMovieClick: (Movie) -> Unit,
    onMoviesListClick: (movies: List<Movie>, title: String) -> Unit,
    metadata: Map<String, Any> = emptyMap(),
) {
    entry<HomeKey>(metadata = metadata) {
        HomeScreen(
            onMovieClick = onMovieClick,
            onMoviesListClick = onMoviesListClick,
        )
    }
}