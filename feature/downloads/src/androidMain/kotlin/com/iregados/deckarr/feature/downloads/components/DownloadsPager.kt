package com.iregados.deckarr.feature.downloads.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.feature.downloads.DownloadsEvent
import com.iregados.deckarr.feature.downloads.DownloadsState
import com.iregados.deckarr.feature.downloads.DownloadsTabsItems
import com.iregados.deckarr.feature.downloads.DownloadsViewModel

@Composable
fun DownloadsPager(
    uiState: DownloadsState,
    modifier: Modifier = Modifier,
    downloadsViewModel: DownloadsViewModel,
    onRemoveTorrent: (id: String) -> Unit
) {
    var expandedTorrentId by remember { mutableStateOf<String?>(null) }

    val downloadsTabsItems = remember(
        uiState.torrents,
        uiState.activeTorrents,
        uiState.downloadingTorrents
    ) {
        listOf(
            DownloadsTabsItems.All(items = uiState.torrents),
            DownloadsTabsItems.Active(items = uiState.activeTorrents),
            DownloadsTabsItems.Downloading(items = uiState.downloadingTorrents)
        )
    }
    val selectedTabIndex = remember(uiState.selectedTab) {
        downloadsTabsItems.indexOfFirst { it.title == uiState.selectedTab.title }
    }
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
    ) { downloadsTabsItems.size }

    LaunchedEffect(uiState.selectedTab) {
        val newPageIndex = downloadsTabsItems.indexOfFirst { it.title == uiState.selectedTab.title }
        expandedTorrentId = null
        pagerState.animateScrollToPage(newPageIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            if (pagerState.currentPage != selectedTabIndex) {
                downloadsViewModel.onEvent(
                    DownloadsEvent.SetSelectedTab(downloadsTabsItems[pagerState.currentPage])
                )
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            downloadsTabsItems.forEach { tabItem ->
                Tab(
                    modifier = Modifier,
                    selected = tabItem.title == uiState.selectedTab.title,
                    onClick = {
                        if (tabItem.title != uiState.selectedTab.title) {
                            downloadsViewModel.onEvent(
                                DownloadsEvent.SetSelectedTab(tabItem)
                            )
                        }
                    },
                    text = {
                        Text(
                            text = tabItem.title,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.Top,
        ) {
            downloadsTabsItems[it].items?.let { torrentList ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    items(
                        count = torrentList.size,
                        key = { index -> torrentList[index].id }
                    ) { index ->
                        val isExpanded = expandedTorrentId == torrentList[index].id
                        TorrentItem(
                            torrent = torrentList[index],
                            isExpanded = isExpanded,
                            isUpdating = uiState.isUpdatingTorrentId == torrentList[index].id,
                            onExpand = {
                                expandedTorrentId = if (isExpanded) null else torrentList[index].id
                            },
                            onRemoveTorrent = { id ->
                                onRemoveTorrent(id)
                            },
                            downloadsViewModel = downloadsViewModel
                        )
                        if (index < torrentList.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}