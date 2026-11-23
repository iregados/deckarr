package com.iregados.deckarr.feature.home

import com.iregados.deckarr.core.domain.dto.DownloadsDataState
import com.iregados.deckarr.core.domain.dto.RadarrLibraryState
import com.iregados.deckarr.core.domain.dto.SonarrLibraryState

data class HomeState(
    val radarr: RadarrLibraryState = RadarrLibraryState(),
    val sonarr: SonarrLibraryState = SonarrLibraryState(),
    val downloads: DownloadsDataState = DownloadsDataState(),
)