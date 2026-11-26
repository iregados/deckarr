package com.iregados.deckarr.feature.series.series_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.SonarrRepository
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

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
    val notificationFlow = _notificationFlow.asSharedFlow()

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
                val sonarrId = passedSeries.id
                when {
                    sonarrId != null -> sonarrRepository.getSeriesBySonarrId(sonarrId)
                    passedSeries.tvdbId != 0L -> {
                        when (val lookup =
                            sonarrRepository.getSeriesByTvdbId(passedSeries.tvdbId)) {
                            is ApiResult.Success -> lookup.data.firstOrNull()?.let {
                                ApiResult.Success(it)
                            } ?: ApiResult.Error("Series not found")

                            is ApiResult.Error -> lookup
                        }
                    }

                    else -> {
                        when (val lookup = sonarrRepository.lookupSeries(passedSeries.title)) {
                            is ApiResult.Success -> {
                                val matched = lookup.data.firstOrNull {
                                    it.tmdbId == passedSeries.tmdbId
                                } ?: lookup.data.firstOrNull()
                                matched?.let { ApiResult.Success(it) }
                                    ?: ApiResult.Error("Series not found")
                            }

                            is ApiResult.Error -> lookup
                        }
                    }
                }
            }
            val episodesDeferred = passedSeries.id?.let { sonarrId ->
                async { sonarrRepository.getEpisodesBySonarrId(sonarrId) }
            }

            val qualityProfilesResult = qualityProfilesDeferred.await()
            val seriesDetailResult = seriesDetailDeferred.await()
            val rootFoldersResult = rootFoldersDeferred.await()
            val episodesResult = episodesDeferred?.await()

            val qualityProfiles = qualityProfilesResult.getOrNull()
            val rootFolders = rootFoldersResult.getOrNull()

            val selectedQualityProfileId =
                preferencesRepository.getSonarrQualityProfileId().firstOrNull()
                    ?: qualityProfiles?.last()?.id
            val selectedRootFolderPath =
                preferencesRepository.getSonarrRootFolderPath().firstOrNull()
                    ?: rootFolders?.first()?.path

            _uiState.update {
                it.copy(
                    isLoading = false,
                    series = seriesDetailResult.getOrNull(),
                    episodes = episodesResult?.getOrNull(),
                    sonarrQualityProfiles = qualityProfiles,
                    sonarrRootFolders = rootFolders,
                    selectedQualityProfileId = selectedQualityProfileId,
                    selectedRootFolderPath = selectedRootFolderPath
                )
            }

            val error = (seriesDetailResult as? ApiResult.Error)
                ?: (episodesResult as? ApiResult.Error)
                ?: (qualityProfilesResult as? ApiResult.Error)
                ?: (rootFoldersResult as? ApiResult.Error)

            if (error != null) {
                _notificationFlow.emitError("Error loading series details: ${error.message}")
            }
        }
    }

    fun onEvent(event: SeriesDetailEvent) {
        when (event) {
            is SeriesDetailEvent.AddSeries -> addSeries(
                qualityProfileId = event.qualityProfileId,
                rootFolderPath = event.rootFolderPath,
                shouldMonitor = event.shouldMonitor,
            )

            is SeriesDetailEvent.RemoveSeries -> removeSeries(
                addToExclusionList = event.addToExclusionList,
                deleteFiles = event.deleteFiles,
            )
        }
    }

    private fun addSeries(
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean,
    ) {
        viewModelScope.launch {
            val series = _uiState.value.series
            if (series == null) {
                _notificationFlow.emitError("Error adding series: series is not loaded")
                return@launch
            }

            preferencesRepository.setSonarrRootFolderPath(rootFolderPath)
            preferencesRepository.setSonarrQualityProfileId(qualityProfileId)

            _uiState.update {
                it.copy(
                    isAdding = true
                )
            }

            when (
                val result = sonarrRepository.addSeries(
                    series = series,
                    qualityProfileId = qualityProfileId,
                    rootFolderPath = rootFolderPath,
                    shouldMonitor = shouldMonitor
                )
            ) {
                is ApiResult.Success -> {
                    val newSeries = result.data
                    delay(500.milliseconds)
                    val episodesResult = newSeries.id
                        ?.let { sonarrRepository.getEpisodesBySonarrId(it) }

                    _uiState.update {
                        it.copy(
                            series = newSeries,
                            isAdding = false,
                            episodes = episodesResult?.getOrNull() ?: it.episodes,
                        )
                    }

                    val episodesError = episodesResult as? ApiResult.Error
                    if (episodesError != null) {
                        _notificationFlow.emitError(
                            "Series was added, but its episodes could not be loaded: " +
                                    episodesError.message
                        )
                    }
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isAdding = false,
                        )
                    }
                    _notificationFlow.emitError("Error adding series: ${result.message}")
                }
            }
        }
    }

    private fun removeSeries(
        addToExclusionList: Boolean,
        deleteFiles: Boolean,
    ) {
        viewModelScope.launch {
            val currentSeries = _uiState.value.series
            val oldId = currentSeries?.id
            if (oldId == null) {
                _notificationFlow.emitError("Error removing series: series is not loaded")
                return@launch
            }

            _uiState.update {
                it.copy(
                    isRemoving = true,
                )
            }

            when (
                val removeResult = sonarrRepository.removeSeriesById(
                    seriesId = oldId,
                    deleteFiles = deleteFiles,
                    addImportExclusion = addToExclusionList
                )
            ) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isRemoving = false,
                        )
                    }
                    _notificationFlow.emitError("Error removing series: ${removeResult.message}")
                    return@launch
                }

                is ApiResult.Success -> if (!removeResult.data) {
                    _uiState.update {
                        it.copy(
                            isRemoving = false,
                        )
                    }
                    _notificationFlow.emitError("Error removing series")
                    return@launch
                }
            }

            when (val seriesDetailResult =
                sonarrRepository.getSeriesByTvdbId(currentSeries.tvdbId)) {
                is ApiResult.Success -> {
                    val seriesDetail = seriesDetailResult.data.firstOrNull()
                    if (seriesDetail != null) {
                        _uiState.update {
                            it.copy(
                                isRemoving = false,
                                series = seriesDetail,
                                episodes = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isRemoving = false,
                            )
                        }
                        _notificationFlow.emitError(
                            "Error getting new detail for series, but series was removed"
                        )
                    }
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isRemoving = false,
                        )
                    }
                    _notificationFlow.emitError(
                        "Series was removed, but its new details could not be loaded: " +
                                seriesDetailResult.message
                    )
                }
            }
        }
    }
}