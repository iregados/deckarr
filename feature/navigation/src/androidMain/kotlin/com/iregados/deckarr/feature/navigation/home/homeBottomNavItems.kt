package com.iregados.deckarr.feature.navigation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.downloads.navigation.keys.DownloadsKey
import com.iregados.deckarr.feature.movies.movies.navigation.keys.MoviesKey
import com.iregados.deckarr.feature.navigation.keys.HomeKey
import com.iregados.deckarr.feature.series.series.navigation.keys.SeriesKey

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: NavKey
)

val homeBottomNavItems = listOf(
    BottomNavItem("Home", Icons.Outlined.Home, HomeKey),
    BottomNavItem("Movies", Icons.Outlined.Movie, MoviesKey),
    BottomNavItem("Series", Icons.Outlined.Tv, SeriesKey),
    BottomNavItem("Downloads", Icons.Outlined.Download, DownloadsKey),
)