package com.iregados.deckarr.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.core.domain.DownloadsRepository
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.domain.SonarrRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val radarrRepository: RadarrRepository,
    private val sonarrRepository: SonarrRepository,
    private val downloadsRepository: DownloadsRepository,
) : ViewModel() {
    val uiState: StateFlow<HomeState> = combine(
        radarrRepository.libraryState,
        sonarrRepository.libraryState,
        downloadsRepository.dataState,
    ) { radarr, sonarr, downloads ->
        HomeState(
            radarr = radarr,
            sonarr = sonarr,
            downloads = downloads,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState(
            radarr = radarrRepository.libraryState.value,
            sonarr = sonarrRepository.libraryState.value,
            downloads = downloadsRepository.dataState.value,
        )
    )
}