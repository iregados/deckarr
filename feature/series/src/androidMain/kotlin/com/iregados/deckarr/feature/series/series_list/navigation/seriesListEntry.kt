package com.iregados.deckarr.feature.series.series_list.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.series.series_list.SeriesList
import com.iregados.deckarr.feature.series.series_list.navigation.keys.SeriesListKey

fun EntryProviderScope<NavKey>.seriesListEntry(
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit
) {
    entry<SeriesListKey> { key ->
        SeriesList(
            passedSeries = key.series,
            title = key.title,
            popMainBackStack = popMainBackStack,
            navigateMainTo = navigateMainTo
        )
    }
}
