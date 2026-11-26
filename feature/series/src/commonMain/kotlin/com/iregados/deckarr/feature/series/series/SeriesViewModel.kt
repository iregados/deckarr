package com.iregados.deckarr.feature.series.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.core.domain.SonarrRepository
import com.iregados.deckarr.core.domain.dto.SonarrLibraryState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SeriesViewModel(
    private val sonarrRepository: SonarrRepository
) : ViewModel() {
    val uiState: StateFlow<SeriesState> = sonarrRepository.libraryState
        .map { it.toSeriesState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            sonarrRepository.libraryState.value.toSeriesState()
        )

    private fun SonarrLibraryState.toSeriesState() = SeriesState(
        isLoading = isLoading,
        needsConfiguration = needsConfiguration,
        error = error,
        recommendedSeries = recommendedSeries,
        rootFolders = rootFolders,
    ).withSeries(series)
}