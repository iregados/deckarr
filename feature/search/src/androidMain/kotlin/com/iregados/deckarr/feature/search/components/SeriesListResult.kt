package com.iregados.deckarr.feature.search.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iregados.api.common.dto.CoverType
import com.iregados.api.sonarr.dto.Series
import com.iregados.deckarr.core.presentation.components.CustomAsyncImage
import deckarr.feature.search.generated.resources.Res
import deckarr.feature.search.generated.resources.sonarr
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SeriesListResult(
    series: List<Series>?,
    needsConfiguration: Boolean,
    onSelect: (series: Series) -> Unit,
) {
    if (needsConfiguration) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Please set up your Sonarr server",
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    if (series == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error while searching",
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    if (series.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No results found")
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            series.size,
            key = { index -> series[index].tvdbId }
        ) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clickable {
                        onSelect(series[index])
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomAsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(0.667f),
                    model = series[index].images.firstOrNull { it.coverType == CoverType.Poster }?.remoteUrl
                        ?: "",
                )
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = series[index].title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = series[index].year.toString(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        if (series[index].id != null) {
                            Image(
                                modifier = Modifier
                                    .size(20.dp),
                                imageVector = vectorResource(Res.drawable.sonarr),
                                contentDescription = "Sonarr",
                            )
                        }
                    }

                    Text(
                        text = series[index].overview ?: "",
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}