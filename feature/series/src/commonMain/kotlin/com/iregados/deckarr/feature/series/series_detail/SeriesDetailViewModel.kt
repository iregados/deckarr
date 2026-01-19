package com.iregados.deckarr.feature.series.series_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.api.sonarr.dto.Series
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.SonarrRepository
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeriesDetailViewModel(
    private val sonarrRepository: SonarrRepository,
    private val preferencesRepository: PreferencesRepository,
    private val passedSeries: Series
) : ViewModel() {
    private val _uiState: MutableStateFlow<SeriesDetailState> =
        MutableStateFlow(SeriesDetailState())
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _uiState.value
        )
    private val _notificationFlow = MutableSharedFlow<Notification>(replay = 0)
    val notificationFlow: SharedFlow<Notification> = _notificationFlow

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                )
            }

            val qualityProfilesDeferred = async { sonarrRepository.getQualityProfiles() }
            val rootFoldersDeferred = async { sonarrRepository.getRootFolders() }
            val seriesDetailDeferred = async {
                passedSeries.id?.let { sonarrId ->
                    sonarrRepository.getSeriesBySonarrId(sonarrId)
                } ?: run {
                    sonarrRepository.getSeriesByTvdbId(passedSeries.tvdbId)?.first()
                }
            }

            val episodes = passedSeries.id?.let { sonarrId ->
                val episodesDeferred = async { sonarrRepository.getEpisodesBySonarrId(sonarrId) }
                episodesDeferred.await()
            }

            val qualityProfiles = qualityProfilesDeferred.await()
            val seriesDetail = seriesDetailDeferred.await()
            val rootFolders = rootFoldersDeferred.await()

            val selectedQualityProfileId =
                preferencesRepository.getSonarrQualityProfileId().firstOrNull()
                    ?: qualityProfiles?.last()?.id
            val selectedRootFolderPath =
                preferencesRepository.getSonarrRootFolderPath().firstOrNull()
                    ?: rootFolders?.first()?.path


            _uiState.update {
                it.copy(
                    isLoading = false,
                    series = seriesDetail,
                    episodes = episodes,
                    sonarrQualityProfiles = qualityProfiles,
                    sonarrRootFolders = rootFolders,
                    selectedQualityProfileId = selectedQualityProfileId,
                    selectedRootFolderPath = selectedRootFolderPath
                )
            }
        }
    }

    fun addSeries(
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean,
        afterAddSeries: (series: Series?) -> Unit = {}
    ) {
        viewModelScope.launch {
            preferencesRepository.setSonarrRootFolderPath(rootFolderPath)
            preferencesRepository.setSonarrQualityProfileId(qualityProfileId)

            _uiState.update {
                it.copy(
                    isAdding = true
                )
            }

            val newSeries = sonarrRepository.addSeries(
                series = _uiState.value.series!!,
                qualityProfileId = qualityProfileId,
                rootFolderPath = rootFolderPath,
                shouldMonitor = shouldMonitor
            )
            if (newSeries != null) {
                delay(500)
                val episodes = sonarrRepository.getEpisodesBySonarrId(newSeries.id!!)
                _uiState.update {
                    it.copy(
                        series = newSeries,
                        isAdding = false,
                        episodes = episodes,
                    )
                }
                afterAddSeries(newSeries)
            } else {
                _uiState.update {
                    it.copy(
                        isAdding = false,
                    )
                }
                _notificationFlow.emitError("Failed to add series")
            }
        }
    }

    fun removeSeries(
        addToExclusionList: Boolean,
        deleteFiles: Boolean,
        afterRemoveSeries: (seriesId: Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRemoving = true,
                )
            }

            val oldId = _uiState.value.series!!.id!!
            val removed = sonarrRepository.removeSeriesById(
                seriesId = oldId,
                deleteFiles = deleteFiles,
                addImportExclusion = addToExclusionList
            )
            if (removed == true) {
                val seriesDetail = sonarrRepository.getSeriesByTvdbId(passedSeries.tvdbId)
                if (!seriesDetail.isNullOrEmpty()) {
                    _uiState.update {
                        it.copy(
                            isRemoving = false,
                            series = seriesDetail.first(),
                            episodes = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isRemoving = false,
                        )
                    }
                    _notificationFlow.emitError("Error getting new detail for series, but series was removed")
                }
                afterRemoveSeries(oldId)
            } else {
                _uiState.update {
                    it.copy(
                        isRemoving = false,
                    )
                }
                _notificationFlow.emitError("Error removing series")
            }
        }
    }
}