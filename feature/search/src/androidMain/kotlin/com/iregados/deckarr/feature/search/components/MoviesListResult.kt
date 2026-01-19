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
import com.iregados.api.radarr.dto.Movie
import com.iregados.deckarr.core.presentation.components.CustomAsyncImage
import deckarr.feature.search.generated.resources.Res
import deckarr.feature.search.generated.resources.radarr
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MoviesListResult(
    movies: List<Movie>?,
    needsConfiguration: Boolean,
    onSelect: (movie: Movie) -> Unit,
) {
    if (needsConfiguration) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Please set up your Radarr server",
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    if (movies == null) {
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

    if (movies.isEmpty()) {
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
            movies.size,
            key = { index -> movies[index].tmdbId }
        ) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clickable {
                        onSelect(movies[index])
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomAsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(0.667f),
                    model = movies[index].images.firstOrNull { it.coverType == CoverType.Poster }?.remoteUrl
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
                                text = movies[index].title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = movies[index].year.toString(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        if (movies[index].id != null) {
                            Image(
                                modifier = Modifier
                                    .size(20.dp),
                                imageVector = vectorResource(Res.drawable.radarr),
                                contentDescription = "Radarr",
                            )
                        }
                    }

                    Text(
                        text = movies[index].overview,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}