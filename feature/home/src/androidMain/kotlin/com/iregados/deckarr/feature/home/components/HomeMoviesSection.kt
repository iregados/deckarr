package com.iregados.deckarr.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.core.components.EmptyListPlaceholder
import com.iregados.deckarr.core.components.MediaListSkeleton
import com.iregados.deckarr.core.components.MovieCard
import com.iregados.deckarr.core.components.MovieListHeader

@Composable
fun HomeMoviesSection(
    title: String,
    emptyMessage: String,
    movies: List<Movie>,
    isLoading: Boolean,
    onMovieClick: (Movie) -> Unit,
    onMoviesListClick: (movies: List<Movie>, title: String) -> Unit,
) {
    MovieListHeader(
        title = title,
        enabled = movies.isNotEmpty(),
        onClick = { headerTitle ->
            onMoviesListClick(
                movies,
                headerTitle
            )
        }
    )

    when {
        isLoading && movies.isEmpty() -> MediaListSkeleton()
        movies.isEmpty() -> EmptyListPlaceholder(
            message = emptyMessage,
            icon = Icons.Outlined.Movie
        )

        else -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    movies.size,
                    key = { index -> movies[index].tmdbId }
                ) { index ->
                    MovieCard(
                        modifier = Modifier
                            .height(120.dp)
                            .width(200.dp),
                        movie = movies[index],
                        onClick = { onMovieClick(movies[index]) }
                    )
                }
            }
        }
    }
}