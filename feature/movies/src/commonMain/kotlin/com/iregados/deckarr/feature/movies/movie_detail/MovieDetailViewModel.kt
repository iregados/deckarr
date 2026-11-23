package com.iregados.deckarr.feature.movies.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val radarrRepository: RadarrRepository,
    private val preferencesRepository: PreferencesRepository,
    private val passedMovie: Movie
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovieDetailState> =
        MutableStateFlow(
            MovieDetailState()
        )
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

            val qualityProfilesDeferred = async { radarrRepository.getQualityProfiles() }
            val rootFoldersDeferred = async { radarrRepository.getRootFolders() }
            val movieDetailDeferred = async {
                val added = radarrRepository.getAddedMovieByTmdbId(passedMovie.tmdbId)
                if (added is ApiResult.Success) {
                    added
                } else {
                    radarrRepository.findMovieByTmdbId(passedMovie.tmdbId)
                }
            }

            val qualityProfilesResult = qualityProfilesDeferred.await()
            val rootFoldersResult = rootFoldersDeferred.await()
            val movieDetailResult = movieDetailDeferred.await()

            val qualityProfiles = qualityProfilesResult.getOrNull()
            val rootFolders = rootFoldersResult.getOrNull()

            val selectedQualityProfileId =
                preferencesRepository.getRadarrQualityProfileId().firstOrNull()
                    ?: qualityProfiles?.last()?.id
            val selectedRootFolderPath =
                preferencesRepository.getRadarrRootFolderPath().firstOrNull()
                    ?: rootFolders?.first()?.path

            _uiState.update {
                it.copy(
                    isLoading = false,
                    movie = movieDetailResult.getOrNull(),
                    radarrQualityProfiles = qualityProfiles,
                    radarrRootFolders = rootFolders,
                    selectedQualityProfileId = selectedQualityProfileId,
                    selectedRootFolderPath = selectedRootFolderPath
                )
            }

            val error = (movieDetailResult as? ApiResult.Error)
                ?: (rootFoldersResult as? ApiResult.Error)
                ?: (qualityProfilesResult as? ApiResult.Error)

            if (error != null) {
                _notificationFlow.emitError("Error loading movie details: ${error.message}")
            }
        }
    }

    fun onEvent(event: MovieDetailEvent) {
        when (event) {
            is MovieDetailEvent.AddMovie -> addMovie(
                qualityProfileId = event.qualityProfileId,
                rootFolderPath = event.rootFolderPath,
                shouldMonitor = event.shouldMonitor,
            )

            is MovieDetailEvent.RemoveMovie -> removeMovie(
                addToExclusionList = event.addToExclusionList,
                deleteFiles = event.deleteFiles,
            )
        }
    }

    private fun addMovie(
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean,
    ) {
        viewModelScope.launch {
            val movie = _uiState.value.movie
            if (movie == null) {
                _notificationFlow.emitError("Error adding movie: movie is not loaded")
                return@launch
            }

            _uiState.update {
                it.copy(
                    isAdding = true,
                )
            }
            preferencesRepository.setRadarrRootFolderPath(rootFolderPath)
            preferencesRepository.setRadarrQualityProfileId(qualityProfileId)

            val result = radarrRepository.addMovie(
                movie = movie,
                qualityProfileId = qualityProfileId,
                rootFolderPath = rootFolderPath,
                shouldMonitor = shouldMonitor
            )

            when (result) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        movie = result.data,
                        isAdding = false,
                    )
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isAdding = false,
                        )
                    }
                    _notificationFlow.emitError("Error adding movie: ${result.message}")
                }
            }
        }
    }

    private fun removeMovie(
        addToExclusionList: Boolean,
        deleteFiles: Boolean,
    ) {
        viewModelScope.launch {
            val oldId = _uiState.value.movie?.id
            if (oldId == null) {
                _notificationFlow.emitError("Error removing movie: movie is not loaded")
                return@launch
            }

            _uiState.update {
                it.copy(
                    isRemoving = true,
                )
            }

            val removeResult = radarrRepository.removeMovieById(
                movieId = oldId,
                deleteFiles = deleteFiles,
                addImportExclusion = addToExclusionList
            )

            if (removeResult is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        isRemoving = false,
                    )
                }
                _notificationFlow.emitError("Error removing movie: ${removeResult.message}")
                return@launch
            }

            when (val movieDetail = radarrRepository.findMovieByTmdbId(passedMovie.tmdbId)) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        isRemoving = false,
                        movie = movieDetail.data,
                    )
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isRemoving = false,
                        )
                    }
                    _notificationFlow.emitError(
                        "Movie was removed, but its new details could not be loaded: ${movieDetail.message}"
                    )
                }
            }
        }
    }
}