package com.iregados.deckarr.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

@Composable
fun rememberNavigationState(
    startTab: BottomNavTab = BottomNavTab.HOME,
): NavigationState {
    val activeTabState = rememberSaveable(
        stateSaver = Saver(
            save = { it.name },
            restore = { name -> BottomNavTab.entries.find { it.name == name } ?: startTab }
        )
    ) {
        mutableStateOf(startTab)
    }

    val backStacks = BottomNavTab.entries.associateWith { tab ->
        key(tab) {
            rememberNavBackStack(navSavedStateConfig, tab.key)
        }
    }

    return remember(startTab, activeTabState, backStacks) {
        NavigationState(
            startTab = startTab,
            activeTabState = activeTabState,
            backStacks = backStacks,
        )
    }
}

@Stable
class NavigationState(
    val startTab: BottomNavTab,
    activeTabState: MutableState<BottomNavTab>,
    val backStacks: Map<BottomNavTab, NavBackStack<NavKey>>,
) {
    var activeTab: BottomNavTab by activeTabState

    val stacksInUse: List<BottomNavTab>
        get() = if (activeTab == startTab) {
            listOf(startTab)
        } else {
            listOf(startTab, activeTab)
        }

}

@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): List<NavEntry<NavKey>> {
    val decoratedEntries = backStacks.mapValues { (tab, stack) ->
        key(tab) {
            val decorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
                rememberViewModelStoreNavEntryDecorator<NavKey>(),
            )

            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = decorators,
                entryProvider = entryProvider,
            )
        }
    }

    return stacksInUse.flatMap { decoratedEntries.getValue(it) }
}