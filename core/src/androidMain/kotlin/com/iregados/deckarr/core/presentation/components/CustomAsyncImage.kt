package com.iregados.deckarr.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter

@Composable
fun CustomAsyncImage(
    modifier: Modifier = Modifier,
    model: String,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.FillHeight
) {
    val asyncPainter = rememberAsyncImagePainter(
        model = model
    )
    val state by asyncPainter.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
    ) {
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }

            is AsyncImagePainter.State.Error -> {
                Image(
                    painter = rememberVectorPainter(Icons.Default.BrokenImage),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }

            else -> {
                Image(
                    painter = asyncPainter,
                    contentDescription = contentDescription,
                    modifier = Modifier,
                    contentScale = contentScale
                )
            }
        }
    }
}