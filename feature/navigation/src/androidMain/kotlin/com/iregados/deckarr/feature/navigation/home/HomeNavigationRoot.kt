package com.iregados.deckarr.feature.navigation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.core.components.SmartSnackBar
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.feature.navigation.BottomNavTab
import com.iregados.deckarr.feature.search.navigation.keys.SearchKey
import com.iregados.deckarr.feature.settings.navigation.keys.SettingsKey
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun HomeNavigationRoot(
    activeTab: BottomNavTab,
    showSnackbar: Boolean,
    onNavigate: (NavKey) -> Unit,
    notificationFlow: SharedFlow<Notification>,
    onRefresh: () -> Unit,
    onTabSelect: (BottomNavTab) -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            if (showSnackbar) {
                SmartSnackBar(notificationFlow)
            }
        },
        bottomBar = {
            NavigationBar {
                BottomNavTab.entries.forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        selected = activeTab == tab,
                        onClick = { onTabSelect(tab) }
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(activeTab.label) },
                actions = {
                    if (activeTab != BottomNavTab.DOWNLOADS) {
                        IconButton(onClick = { onNavigate(SearchKey) }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }

                    IconButton(onClick = { onNavigate(SettingsKey) }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isRefreshing = false,
            onRefresh = onRefresh
        ) {
            content()
        }
    }
}