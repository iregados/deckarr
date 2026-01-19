package com.iregados.deckarr.feature.movies.movie_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iregados.api.common.dto.CoverType
import com.iregados.api.radarr.dto.Movie
import com.iregados.deckarr.core.presentation.components.CustomAsyncImage

@Composable
fun MovieDetailHeader(
    modifier: Modifier = Modifier,
    movie: Movie,
    onBackClick: () -> Unit,
    paddingValues: PaddingValues,
) {
    val height = remember {
        paddingValues.calculateTopPadding() + 210.dp
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        CustomAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.778f),
            model = movie.images.firstOrNull { it.coverType == CoverType.FanArt }?.remoteUrl ?: "",
            contentDescription = movie.title,
            contentScale = ContentScale.Fit
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.778f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = .2f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            CustomAsyncImage(
                modifier = Modifier
                    .height(150.dp)
                    .aspectRatio(.667f),
                model = movie.images.firstOrNull { it.coverType == CoverType.Poster }?.remoteUrl
                    ?: "",
                contentDescription = movie.title
            )

            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(paddingValues)
                .padding(start = 4.dp),
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }
}