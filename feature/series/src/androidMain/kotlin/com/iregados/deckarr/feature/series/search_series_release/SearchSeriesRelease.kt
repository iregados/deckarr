package com.iregados.deckarr.feature.series.search_series_release

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iregados.deckarr.core.util.extension.toFormatedSize
import com.iregados.deckarr.feature.series.search_series_release.navigation.keys.util.SearchSeriesReleaseParams
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SearchSeriesRelease(
    params: SearchSeriesReleaseParams,
    viewModel: SearchSeriesReleaseViewModel = koinViewModel(
        parameters = { parametersOf(params) }
    ),
    popMainBackStack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Search") },
                navigationIcon = {
                    IconButton(
                        onClick = { popMainBackStack() }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            "Close"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.TopStart
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            return@Scaffold
        }

        if (uiState.sonarrReleases == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error while loading releases",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            return@Scaffold
        }

        uiState.sonarrReleases?.let { releases ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(releases.size, key = { index ->
                        releases[index].guid + releases[index].infoUrl
                    }) { index ->
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    viewModel.addRelease(
                                        sonarrRelease = releases[index],
                                    )
                                }
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = releases[index].title,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (releases[index].approved) {
                                    Icon(
                                        modifier = Modifier
                                            .size(18.dp),
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Approved",
                                        tint = Color.Green.copy(green = .6f)
                                    )
                                } else {
                                    Icon(
                                        modifier = Modifier
                                            .size(18.dp),
                                        imageVector = Icons.Default.DoNotDisturb,
                                        contentDescription = "Deny",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier,
                                            text = "Size: ${releases[index].size.toFormatedSize()}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            modifier = Modifier,
                                            text = releases[index].indexer,
                                            style = MaterialTheme.typography.bodySmall,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier,
                                            text = releases[index].quality.quality.name,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            modifier = Modifier
                                                .weight(1f),
                                            text = "Res.: " + (if (releases[index].quality.quality.resolution == 0L) "Unknown" else releases[index].quality.quality.resolution),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            modifier = Modifier,
                                            text = "S-${releases[index].seeders} / L-${releases[index].leechers}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }

                            if (uiState.grabbingReleasesIds.contains(releases[index].guid)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            if (uiState.grabbedReleasesIds.contains(releases[index].guid)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Grabbed",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                            if (uiState.errorWhileGrabbingReleasesIds.contains(releases[index].guid)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Error while grabbing",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
