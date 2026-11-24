package com.iregados.deckarr.feature.search.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.feature.search.SearchScreen
import com.iregados.deckarr.feature.search.navigation.keys.SearchKey

fun EntryProviderScope<NavKey>.searchEntry(
    popMainBackStack: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    onSeriesClick: (Series) -> Unit,
) {
    entry<SearchKey> {
        SearchScreen(
            popMainBackStack = popMainBackStack,
            onMovieClick = onMovieClick,
            onSeriesClick = onSeriesClick,
        )
    }
}