package com.iregados.deckarr.feature.movies.movies.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.movies.movies.MoviesScreen
import com.iregados.deckarr.feature.movies.movies.navigation.keys.MoviesKey

fun EntryProviderScope<NavKey>.moviesEntry(
    navigateMainTo: (NavKey) -> Unit,
    metadata: Map<String, Any> = emptyMap(),
) {
    entry<MoviesKey>(metadata = metadata) {
        MoviesScreen(
            navigateMainTo = navigateMainTo
        )
    }
}
