package com.iregados.deckarr.feature.series.search_series_release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.core.domain.SonarrRepository
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.feature.series.search_series_release.navigation.keys.util.SearchSeriesReleaseParams
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchSeriesReleaseViewModel(
    private val sonarrRepository: SonarrRepository,
    private val params: SearchSeriesReleaseParams
) : ViewModel() {
    private val _uiState: MutableStateFlow<SearchSeriesReleaseState> =
        MutableStateFlow(SearchSeriesReleaseState())
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _uiState.value
        )

    private val _notificationFlow = MutableSharedFlow<Notification>(replay = 0)
    val notificationFlow = _notificationFlow.asSharedFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }

            val result = when (params) {
                is SearchSeriesReleaseParams.ByEpisode -> {
                    sonarrRepository.getReleases(params.episodeId)
                }

                is SearchSeriesReleaseParams.BySeriesAndSeason -> {
                    sonarrRepository.getReleases(
                        seriesId = params.seriesId,
                        seasonNumber = params.seasonNumber
                    )
                }
            }
            val releases = result.getOrNull()
                ?.filter { it.protocol == "torrent" }
                ?.sortedBy { it.indexer }
                ?.sortedBy { it.size }
                ?.sortedByDescending { it.approved }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    sonarrReleases = releases,
                    error = (result as? ApiResult.Error)?.message,
                )
            }
        }
    }


    fun addRelease(
        sonarrRelease: com.iregados.deckarr.api.sonarr.dto.SonarrRelease,
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    grabbingReleasesIds = it.grabbingReleasesIds + sonarrRelease.guid
                )
            }

            sonarrRepository.addRelease(
                sonarrRelease = sonarrRelease
            ).getOrNull()?.let {
                _uiState.update {
                    it.copy(
                        grabbingReleasesIds = it.grabbingReleasesIds - sonarrRelease.guid,
                        grabbedReleasesIds = it.grabbedReleasesIds + sonarrRelease.guid
                    )
                }

            } ?: run {
                _uiState.update {
                    it.copy(
                        grabbingReleasesIds = it.grabbingReleasesIds - sonarrRelease.guid,
                        errorWhileGrabbingReleasesIds = it.errorWhileGrabbingReleasesIds + sonarrRelease.guid
                    )
                }
            }
        }
    }
}