package com.iregados.deckarr.feature.movies.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.api.radarr.dto.Movie
import com.iregados.deckarr.core.domain.RadarrRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val radarrRepository: RadarrRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<MoviesState> = MutableStateFlow(MoviesState())
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _uiState.value
        )

    init {
        radarrRepository.getConfigFlow()
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
            val moviesDeferred = async { radarrRepository.getMovies() }
            val importListDeferred = async {
                radarrRepository.getImportListMovies(
                    includeRecommendations = true,
                    includeTrending = true,
                    includePopular = true
                )
            }
            val rootFoldersDeferred = async {
                radarrRepository.getRootFolders()
            }

            val movies = moviesDeferred.await()
            val importList = importListDeferred.await()
            val rootFolders = rootFoldersDeferred.await()

            val recommendations = importList?.recommendations
            val trending = importList?.trending
            val popular = importList?.popular

            _uiState.update { state ->
                state.copy(
                    movies = movies,
                    missingFileMovies = movies
                        ?.filter { it.hasFile != true }
                        ?.sortedByDescending { it.added },
                    recentlyAddedMovies = movies
                        ?.filter { it.hasFile == true }
                        ?.sortedByDescending { it.added },
                    recommendedMovies = recommendations,
                    trendingMovies = trending,
                    popularMovies = popular,
                    rootFolders = rootFolders,
                    isLoading = false
                )
            }
        }
    }

    fun addMovieToState(newMovie: Movie) {
        val newList = _uiState.value.movies?.plus(newMovie)
        _uiState.update { state ->
            state.copy(
                movies = newList,
                missingFileMovies = newList
                    ?.filter { it.hasFile != true }
                    ?.sortedByDescending { it.added },
                recentlyAddedMovies = newList
                    ?.filter { it.hasFile == true }
                    ?.sortedByDescending { it.added },
            )
        }
    }

    fun removeMovieFromState(movieId: Long) {
        val newList = _uiState.value.movies?.filter { it.id != movieId }
        _uiState.update { state ->
            state.copy(
                movies = newList,
                missingFileMovies = newList
                    ?.filter { it.hasFile != true }
                    ?.sortedByDescending { it.added },
                recentlyAddedMovies = newList
                    ?.filter { it.hasFile == true }
                    ?.sortedByDescending { it.added },
            )
        }
    }
}