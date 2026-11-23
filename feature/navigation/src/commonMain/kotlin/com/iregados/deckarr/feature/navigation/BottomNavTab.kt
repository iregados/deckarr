package com.iregados.deckarr.feature.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.downloads.keys.DownloadsKey
import com.iregados.deckarr.feature.home.navigation.keys.HomeKey
import com.iregados.deckarr.feature.movies.movies.navigation.keys.MoviesKey
import com.iregados.deckarr.feature.series.series.navigation.keys.SeriesKey

enum class BottomNavTab(
    val key: NavKey,
    val label: String,
    val icon: ImageVector,
) {
    HOME(HomeKey, "Home", Icons.Outlined.Home),
    MOVIES(MoviesKey, "Movies", Icons.Outlined.Movie),
    SERIES(
        SeriesKey,
        "Series",
        Icons.Outlined.Tv
    ),
    DOWNLOADS(
        DownloadsKey,
        "Downloads",
        Icons.Outlined.Download
    );

    companion object {
        fun fromKey(key: NavKey?): BottomNavTab? = entries.find { it.key == key }
    }
}