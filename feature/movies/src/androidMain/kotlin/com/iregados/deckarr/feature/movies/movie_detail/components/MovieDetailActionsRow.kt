package com.iregados.deckarr.feature.movies.movie_detail.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.iregados.deckarr.api.radarr.dto.Movie
import deckarr.feature.movies.generated.resources.Res
import deckarr.feature.movies.generated.resources.imdb_mono
import deckarr.feature.movies.generated.resources.youtube
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MovieDetailActionsRow(
    movie: Movie,
    isAdding: Boolean,
    isRemoving: Boolean,
    onSearchReleaseClick: () -> Unit,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            modifier = Modifier
                .width(80.dp)
                .height(48.dp),
            onClick = {
                Intent(
                    Intent.ACTION_VIEW,
                    "https://www.youtube.com/watch?v=${movie.youTubeTrailerId}".toUri()
                ).apply {
                    context.startActivity(this)
                }
            }
        ) {
            Icon(
                vectorResource(Res.drawable.youtube),
                null
            )
        }

        OutlinedButton(
            modifier = Modifier
                .width(80.dp)
                .height(48.dp),
            enabled = movie.imdbId != null,
            onClick = {
                Intent(
                    Intent.ACTION_VIEW,
                    "https://www.imdb.com/title/${movie.imdbId}".toUri()
                ).apply {
                    context.startActivity(this)
                }
            }
        ) {
            Icon(
                vectorResource(Res.drawable.imdb_mono),
                null
            )
        }

        if (movie.id != null) {
            OutlinedButton(
                modifier = Modifier
                    .width(80.dp)
                    .height(48.dp),
                onClick = onSearchReleaseClick
            ) {
                Icon(
                    Icons.Default.Search,
                    null
                )
            }

            OutlinedButton(
                modifier = Modifier
                    .width(80.dp)
                    .height(48.dp),
                onClick = onRemoveClick
            ) {
                if (isRemoving) {
                    CircularProgressIndicator()
                } else {
                    Icon(
                        Icons.Default.Delete,
                        "Delete movie",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        } else {
            OutlinedButton(
                modifier = Modifier
                    .width(80.dp)
                    .height(48.dp),
                onClick = onAddClick
            ) {
                if (isAdding) {
                    CircularProgressIndicator()
                } else {
                    Icon(
                        Icons.Default.Add,
                        "Add movie",
                    )
                }
            }
        }
    }
}