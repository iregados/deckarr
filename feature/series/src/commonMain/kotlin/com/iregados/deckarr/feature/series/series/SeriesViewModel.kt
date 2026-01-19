package com.iregados.deckarr.feature.series.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.api.sonarr.dto.Series
import com.iregados.deckarr.core.domain.SonarrRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeriesViewModel(
    private val sonarrRepository: SonarrRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<SeriesState> = MutableStateFlow(SeriesState())
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _uiState.value
        )

    init {
        sonarrRepository.getConfigFlow()
            .onEach { apiConfig ->
                if (
                    apiConfig.host.isEmpty() ||
                    apiConfig.apiKey.isEmpty()
                ) {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            needsConfiguration = true
                        )
                    }
                } else {
                    _uiState.update { it.copy(needsConfiguration = false) }
                    loadData()
                }
            }
            .launchIn(viewModelScope)
    }

    fun loadData() {
        if (_uiState.value.needsConfiguration) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                )
            }
            val seriesDeferred = async { sonarrRepository.getSeries() }
            val rootFoldersDeferred = async {
                sonarrRepository.getRootFolders()
            }

            val series = seriesDeferred.await()
            val rootFolders = rootFoldersDeferred.await()

            _uiState.update { state ->
                state.copy(
                    series = series,
                    missingFileSeries = series
                        ?.filter { it.statistics.episodeFileCount == 0L },
                    recentlyAddedSeries = series
                        ?.filter { it.statistics.episodeFileCount != 0L },
                    rootFolders = rootFolders,
                    isLoading = false
                )
            }
        }
    }

    fun addSeriesToState(newSeries: Series) {
        val newList = _uiState.value.series
            ?.plus(newSeries)
            ?.sortedByDescending { it.added }
        _uiState.update { state ->
            state.copy(
                series = newList,
                missingFileSeries = newList
                    ?.filter { it.statistics.episodeFileCount == 0L },
                recentlyAddedSeries = newList
                    ?.filter { it.statistics.episodeFileCount != 0L },
            )
        }
    }

    fun removeSeriesFromState(seriesId: Long) {
        val newList = _uiState.value.series
            ?.filter { it.id != seriesId }
        _uiState.update { state ->
            state.copy(
                series = newList,
                missingFileSeries = newList
                    ?.filter { it.statistics.episodeFileCount == 0L },
                recentlyAddedSeries = newList
                    ?.filter { it.statistics.episodeFileCount != 0L },
            )
        }
    }
}