package com.iregados.deckarr.feature.series.search_series_release.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.series.search_series_release.SearchSeriesRelease
import com.iregados.deckarr.feature.series.search_series_release.navigation.keys.SearchSeriesReleaseKey

fun EntryProviderScope<NavKey>.searchSeriesReleaseEntry(
    popMainBackStack: () -> Unit,
) {
    entry<SearchSeriesReleaseKey> { key ->
        SearchSeriesRelease(
            params = key.params,
            popMainBackStack = popMainBackStack
        )
    }
}
