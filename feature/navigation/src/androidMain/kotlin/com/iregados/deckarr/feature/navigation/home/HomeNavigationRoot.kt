package com.iregados.deckarr.feature.navigation.home

import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.iregados.deckarr.core.presentation.components.SmartSnackBar
import com.iregados.deckarr.feature.downloads.DownloadsViewModel
import com.iregados.deckarr.feature.downloads.navigation.downloadsEntry
import com.iregados.deckarr.feature.downloads.navigation.keys.DownloadsKey
import com.iregados.deckarr.feature.home.navigation.homeEntry
import com.iregados.deckarr.feature.movies.movies.MoviesViewModel
import com.iregados.deckarr.feature.movies.movies.navigation.moviesEntry
import com.iregados.deckarr.feature.navigation.NavigationViewModel
import com.iregados.deckarr.feature.navigation.keys.HomeKey
import com.iregados.deckarr.feature.search.navigation.keys.SearchKey
import com.iregados.deckarr.feature.series.series.SeriesViewModel
import com.iregados.deckarr.feature.series.series.navigation.seriesEntry
import com.iregados.deckarr.feature.settings.navigation.keys.SettingsKey

@Composable
fun HomeNavigationRoot(
    navViewModel: NavigationViewModel,
    moviesViewModel: MoviesViewModel,
    seriesViewModel: SeriesViewModel,
    downloadsViewModel: DownloadsViewModel
) {
    val bottomBackStack by navViewModel.bottomBackStack.collectAsStateWithLifecycle()

    val activity = LocalActivity.current

    Scaffold(
        snackbarHost = { SmartSnackBar(downloadsViewModel.notificationFlow) },
        bottomBar = {
            NavigationBar {
                homeBottomNavItems.forEach {
                    NavigationBarItem(
                        icon = { Icon(it.icon, contentDescription = it.label) },
                        label = { Text(it.label) },
                        selected = bottomBackStack.last() == it.route,
                        onClick = { navViewModel.navigateBottomTo(it.route) }
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    homeBottomNavItems.first { it.route == bottomBackStack.last() }.label.let {
                        Text(
                            it
                        )
                    }
                },
                actions = {
                    if (bottomBackStack.last() != DownloadsKey) {
                        IconButton(
                            onClick = {
                                navViewModel.navigateMainTo(SearchKey)
                            }
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            navViewModel.navigateMainTo(SettingsKey)
                        }
                    ) {
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
            onRefresh = {
                downloadsViewModel.loadData()
                moviesViewModel.loadData()
                seriesViewModel.loadData()
            }
        ) {
            NavDisplay(
                modifier = Modifier
                    .fillMaxSize(),
                backStack = bottomBackStack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                onBack = {
                    if (bottomBackStack.last() == HomeKey) {
                        activity?.finish()
                    } else {
                        navViewModel.popBottomBackStack()
                    }
                },
                entryProvider = entryProvider {
                    homeEntry(
                        moviesViewModel = moviesViewModel,
                        seriesViewModel = seriesViewModel,
                        downloadViewModel = downloadsViewModel,
                        navigateMainTo = { navViewModel.navigateMainTo(it) },
                    )
                    moviesEntry(
                        moviesViewModel = moviesViewModel,
                        navigateMainTo = { navViewModel.navigateMainTo(it) }
                    )
                    seriesEntry(
                        seriesViewModel = seriesViewModel,
                        navigateMainTo = { navViewModel.navigateMainTo(it) }
                    )
                    downloadsEntry(
                        downloadsViewModel = downloadsViewModel,
                    )
                }
            )
        }
    }
}