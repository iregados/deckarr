package com.iregados.deckarr.feature.search.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.search.SearchScreen
import com.iregados.deckarr.feature.search.navigation.keys.SearchKey

fun EntryProviderScope<NavKey>.searchEntry(
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit
) {
    entry<SearchKey> { key ->
        SearchScreen(
            popMainBackStack = popMainBackStack,
            navigateMainTo = navigateMainTo
        )
    }
}
