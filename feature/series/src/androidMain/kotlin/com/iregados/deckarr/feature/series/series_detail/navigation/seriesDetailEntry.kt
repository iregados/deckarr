package com.iregados.deckarr.feature.series.series_detail.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.series.series.SeriesViewModel
import com.iregados.deckarr.feature.series.series_detail.SeriesDetailScreen
import com.iregados.deckarr.feature.series.series_detail.navigation.keys.SeriesDetailKey

fun EntryProviderScope<NavKey>.seriesDetailEntry(
    seriesViewModel: SeriesViewModel,
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit
) {
    entry<SeriesDetailKey> { key ->
        SeriesDetailScreen(
            seriesViewModel = seriesViewModel,
            passedSeries = key.series,
            popMainBackStack = popMainBackStack,
            navigateMainTo = navigateMainTo
        )
    }
}
