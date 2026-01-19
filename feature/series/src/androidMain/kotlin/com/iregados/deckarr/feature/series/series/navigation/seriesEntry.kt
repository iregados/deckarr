package com.iregados.deckarr.feature.series.series.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.series.series.SeriesScreen
import com.iregados.deckarr.feature.series.series.SeriesViewModel
import com.iregados.deckarr.feature.series.series.navigation.keys.SeriesKey

fun EntryProviderScope<NavKey>.seriesEntry(
    seriesViewModel: SeriesViewModel,
    navigateMainTo: (NavKey) -> Unit
) {
    entry<SeriesKey> {
        SeriesScreen(
            seriesViewModel = seriesViewModel,
            navigateMainTo = navigateMainTo
        )
    }
}
