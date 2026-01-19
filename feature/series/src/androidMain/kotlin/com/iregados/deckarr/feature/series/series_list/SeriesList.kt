package com.iregados.deckarr.feature.series.series_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.iregados.api.sonarr.dto.Series
import com.iregados.deckarr.core.presentation.components.SeriesCard
import com.iregados.deckarr.feature.series.series_detail.navigation.keys.SeriesDetailKey

@Composable
fun SeriesList(
    passedSeries: List<Series>,
    title: String,
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        title
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { popMainBackStack() }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(passedSeries.size, key = { index -> passedSeries[index].tvdbId }) { index ->
                SeriesCard(
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth(),
                    series = passedSeries[index],
                    onClick = {
                        navigateMainTo(
                            SeriesDetailKey(
                                passedSeries[index]
                            )
                        )
                    }
                )
            }
        }
    }
}