package com.iregados.deckarr.feature.series.series_detail.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.iregados.deckarr.api.sonarr.dto.Series
import deckarr.feature.series.generated.resources.Res
import deckarr.feature.series.generated.resources.imdb_mono
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SeriesDetailActionsRow(
    series: Series,
    isAdding: Boolean,
    isRemoving: Boolean,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        OutlinedButton(
            modifier = Modifier
                .width(80.dp)
                .height(48.dp),
            enabled = series.imdbId != null,
            onClick = {
                Intent(
                    Intent.ACTION_VIEW,
                    "https://www.imdb.com/title/${series.imdbId}".toUri()
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

        if (series.id != null) {
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
                        "Delete series",
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
                        "Add series",
                    )
                }
            }
        }
    }
}