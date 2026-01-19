package com.iregados.deckarr.feature.series.series_detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.iregados.api.sonarr.dto.Episode
import com.iregados.deckarr.core.theme.successGreen
import com.iregados.deckarr.core.theme.warningYellow
import com.iregados.deckarr.core.util.extension.hasDatePassed
import com.iregados.deckarr.feature.series.search_series_release.navigation.keys.SearchSeriesReleaseKey
import com.iregados.deckarr.feature.series.search_series_release.navigation.keys.util.SearchSeriesReleaseParams

@Composable
fun EpisodeBySeasonList(
    episodes: List<Episode>,
    navigateMainTo: (NavKey) -> Unit,
) {
    // Group episodes by season number
    val episodesBySeason = episodes
        .sortedByDescending { it.seasonNumber }
        .groupBy { it.seasonNumber }
    // Track expanded/collapsed state for each season
    val expandedSeasons = remember { mutableStateMapOf<Long, Boolean>() }

    episodesBySeason.forEach { (seasonNumber, seasonEpisodes) ->
        val expanded = expandedSeasons[seasonNumber] ?: false
        val totalEpisodes = seasonEpisodes.size
        val downloadedEpisodes = seasonEpisodes.count { it.hasFile }

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable {
                        expandedSeasons[seasonNumber] = !expanded
                    }
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (seasonNumber == 0L) "Specials" else "S$seasonNumber",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$downloadedEpisodes/$totalEpisodes",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Top)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navigateMainTo(
                                SearchSeriesReleaseKey(
                                    SearchSeriesReleaseParams.BySeriesAndSeason(
                                        seasonEpisodes[0].seriesId,
                                        seasonNumber
                                    )
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier,
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp)
                ) {
                    seasonEpisodes.sortedBy { it.episodeNumber }.forEach { episode ->
                        Row(
                            modifier = Modifier
                                .height(56.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "S${episode.seasonNumber}E${episode.episodeNumber}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                                Text(
                                    text = episode.title ?: "Untitled",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .weight(1f),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (episode.hasFile) {
                                    Icon(
                                        imageVector = Icons.Outlined.CheckCircle,
                                        contentDescription = "Downloaded",
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.successGreen
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.DoNotDisturbOn,
                                        contentDescription = "Downloaded",
                                        modifier = Modifier.size(16.dp),
                                        tint = if (episode.airDate.hasDatePassed()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.warningYellow
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        navigateMainTo(
                                            SearchSeriesReleaseKey(
                                                SearchSeriesReleaseParams.ByEpisode(
                                                    episode.id
                                                )
                                            )
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
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
