package com.iregados.deckarr.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey

@Composable
fun rememberNavigator(navigationState: NavigationState): Navigator {
    return remember(navigationState) { Navigator(navigationState) }
}

class Navigator(
    private val state: NavigationState,
) {
    private val currentStack
        get() = state.backStacks.getValue(state.activeTab)

    fun selectTab(tab: BottomNavTab) {
        if (state.activeTab == tab) {
            if (currentStack.size > 1) {
                currentStack.subList(1, currentStack.size).clear()
            }
        } else {
            state.activeTab = tab
        }
    }

    fun navigate(route: NavKey) {
        val targetTab = BottomNavTab.fromKey(route)
        if (targetTab != null) {
            selectTab(targetTab)
        } else {
            currentStack.add(route)
        }
    }

    fun goBack() {
        if (currentStack.size > 1) {
            currentStack.removeLastOrNull()
        } else if (state.activeTab != state.startTab) {
            state.activeTab = state.startTab
        }
    }
}