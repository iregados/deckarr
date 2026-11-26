package com.iregados.deckarr.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.core.domain.dto.DownloadsDataState
import com.iregados.deckarr.core.domain.dto.RadarrLibraryState
import com.iregados.deckarr.core.domain.dto.SonarrLibraryState
import com.iregados.deckarr.core.util.extension.round
import com.iregados.deckarr.core.util.extension.toFormatedSize

@Composable
fun StatsComponent(
    radarrState: RadarrLibraryState,
    sonarrState: SonarrLibraryState,
    downloadsState: DownloadsDataState,
) {
    HomeCategoryHeader(
        title = "Stats",
        icon = Icons.Outlined.QueryStats,
        topPadding = 8.dp,
        bottomPadding = 12.dp,
    )

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!radarrState.needsConfiguration) {
                StatsSection(
                    title = "Movies",
                    icon = Icons.Outlined.Movie,
                    rows = listOf(
                        "Movies" to (radarrState.movies?.size?.toString() ?: "0"),
                        "Files" to (radarrState.movies?.count { it.movieFile != null }?.toString()
                            ?: "-"),
                        "Total size" to (radarrState.movies?.sumOf { it.movieFile?.size ?: 0L }
                            ?.toFormatedSize() ?: "-"),
                    )
                )
            }

            if (!sonarrState.needsConfiguration) {
                StatsSection(
                    title = "Series",
                    icon = Icons.Outlined.Tv,
                    rows = listOf(
                        "Series" to (sonarrState.series?.size?.toString() ?: "0"),
                        "Files" to (sonarrState.series?.sumOf { it.statistics.episodeFileCount }
                            ?.toString() ?: "-"),
                        "Total size" to (sonarrState.series?.sumOf { it.statistics.sizeOnDisk }
                            ?.toFormatedSize() ?: "-"),
                    )
                )
            }

            if (!downloadsState.needsConfiguration) {
                StatsSection(
                    title = "Downloads",
                    icon = Icons.Outlined.Download,
                    rows = listOf(
                        "Torrents" to (downloadsState.torrents?.size?.toString() ?: "0"),
                        "Ratio" to (downloadsState.stats?.globalRatio?.round(2)?.toString() ?: "-"),
                        "Total size" to (downloadsState.torrents
                            ?.sumOf { it.totalSize ?: 0L }
                            ?.toFormatedSize() ?: "0"),
                    )
                )
            }

            FreeSpaceSection(
                radarrState = radarrState,
                sonarrState = sonarrState,
            )
        }
    }
}