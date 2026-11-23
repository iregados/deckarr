package com.iregados.deckarr.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.core.domain.dto.RadarrLibraryState
import com.iregados.deckarr.core.domain.dto.SonarrLibraryState
import com.iregados.deckarr.core.util.extension.round
import com.iregados.deckarr.core.util.extension.toFormatedSize

@Composable
fun FreeSpaceSection(
    radarrState: RadarrLibraryState,
    sonarrState: SonarrLibraryState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(18.dp),
            imageVector = Icons.Outlined.Storage,
            contentDescription = "Free Space",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Free Space",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }

    radarrState.rootFolders?.forEach { folder ->
        val usedSize = remember(radarrState.movies, folder.path) {
            radarrState.movies?.sumOf { movie ->
                if (movie.rootFolderPath == folder.path) movie.movieFile?.size ?: 0 else 0
            } ?: 0
        }

        RootFolderItem(
            path = folder.path,
            usedSize = usedSize,
            freeSpace = folder.freeSpace,
        )
    }

    sonarrState.rootFolders?.forEach { folder ->
        val usedSize = remember(sonarrState.series, folder.path) {
            sonarrState.series?.sumOf { series ->
                if (series.rootFolderPath == folder.path) series.statistics.sizeOnDisk else 0
            } ?: 0
        }

        RootFolderItem(
            path = folder.path,
            usedSize = usedSize,
            freeSpace = folder.freeSpace,
        )
    }
}

@Composable
private fun RootFolderItem(
    path: String?,
    usedSize: Long,
    freeSpace: Long,
) {
    val total = usedSize + freeSpace
    val progress = if (total > 0L) (usedSize.toFloat() / total).round(1) else 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = path ?: "Unknown",
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${freeSpace.toFormatedSize()} free",
                style = MaterialTheme.typography.labelMedium
            )
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth(),
            trackColor = MaterialTheme.colorScheme.inversePrimary,
        )
    }
}