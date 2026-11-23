package com.iregados.deckarr.feature.series.series.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.series.series.SeriesScreen
import com.iregados.deckarr.feature.series.series.navigation.keys.SeriesKey

fun EntryProviderScope<NavKey>.seriesEntry(
    navigateMainTo: (NavKey) -> Unit,
    metadata: Map<String, Any> = emptyMap(),
) {
    entry<SeriesKey>(metadata = metadata) {
        SeriesScreen(
            navigateMainTo = navigateMainTo
        )
    }
}
