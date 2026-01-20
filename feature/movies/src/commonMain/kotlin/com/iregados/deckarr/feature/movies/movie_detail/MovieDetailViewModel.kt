package com.iregados.deckarr.feature.movies.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.api.radarr.dto.Movie
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
    private val _uiState: MutableStateFlow<MovieDetailState> = MutableStateFlow(MovieDetailState())
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
                radarrRepository.getAddedMovieByTmdbId(passedMovie.tmdbId)
                    ?: radarrRepository.findMovieByTmdbId(passedMovie.tmdbId)
            }

            val rootFolders = rootFoldersDeferred.await()
            val qualityProfiles = qualityProfilesDeferred.await()
            val movieDetail = movieDetailDeferred.await()
            val selectedQualityProfileId =
                preferencesRepository.getRadarrQualityProfileId().firstOrNull()
                    ?: qualityProfiles?.last()?.id
            val selectedRootFolderPath =
                preferencesRepository.getRadarrRootFolderPath().firstOrNull()
                    ?: rootFolders?.first()?.path

            _uiState.update {
                it.copy(
                    isLoading = false,
                    movie = movieDetail,
                    radarrQualityProfiles = qualityProfiles,
                    radarrRootFolders = rootFolders,
                    selectedQualityProfileId = selectedQualityProfileId,
                    selectedRootFolderPath = selectedRootFolderPath
                )
            }
        }
    }

    fun addMovie(
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean,
        afterAddMovie: (movie: Movie?) -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAdding = true,
                )
            }
            preferencesRepository.setRadarrRootFolderPath(rootFolderPath)
            preferencesRepository.setRadarrQualityProfileId(qualityProfileId)

            val newMovie = radarrRepository.addMovie(
                movie = _uiState.value.movie!!,
                qualityProfileId = qualityProfileId,
                rootFolderPath = rootFolderPath,
                shouldMonitor = shouldMonitor
            )

            if (newMovie != null) {
                _uiState.update {
                    it.copy(
                        movie = newMovie,
                        isAdding = false,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isAdding = false,
                    )
                }
                _notificationFlow.emitError("Error adding movie")
            }
            afterAddMovie(newMovie)
        }
    }

    fun removeMovie(
        addToExclusionList: Boolean,
        deleteFiles: Boolean,
        afterRemoveMovie: (movieId: Long) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRemoving = true,
                )
            }

            val oldId = _uiState.value.movie!!.id!!
            val removed = radarrRepository.removeMovieById(
                movieId = oldId,
                deleteFiles = deleteFiles,
                addImportExclusion = addToExclusionList
            )
            if (removed == true) {
                val movieDetail = radarrRepository.findMovieByTmdbId(passedMovie.tmdbId)
                if (movieDetail != null) {
                    _uiState.update {
                        it.copy(
                            isRemoving = false,
                            movie = movieDetail,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isRemoving = false,
                        )
                    }
                    _notificationFlow.emitError("Error getting new detail for movie, but movie was removed")
                }
                afterRemoveMovie(oldId)
            } else {
                _uiState.update {
                    it.copy(
                        isRemoving = false,
                    )
                }
                _notificationFlow.emitError("Error removing movie")
            }
        }
    }
}