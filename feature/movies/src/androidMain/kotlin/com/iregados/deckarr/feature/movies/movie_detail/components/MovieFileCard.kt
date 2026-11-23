package com.iregados.deckarr.feature.movies.movie_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.core.util.extension.toFormatedSize
import com.iregados.deckarr.core.util.extension.toFormattedDate

@Composable
fun MovieFileCard(
    movie: Movie,
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (movie.id != null) {
                Text(
                    "Movie is added to Radarr",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Added",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            movie.added.toFormattedDate(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                movie.movieFile?.let {
                    Text(
                        "Movie file available",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            it.path,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Added",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    it.dateAdded.toFormattedDate(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Size on disk",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    it.size.toFormatedSize(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Group",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    it.releaseGroup ?: "Unknown",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                } ?: run {
                    Text(
                        "Movie file is not available",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                Text(
                    "Movie is not added to Radarr",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}