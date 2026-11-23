package com.iregados.deckarr.feature.movies.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.domain.dto.RadarrLibraryState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MoviesViewModel(
    private val radarrRepository: RadarrRepository
) : ViewModel() {
    val uiState: StateFlow<MoviesState> = radarrRepository.libraryState
        .map { it.toMoviesState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            radarrRepository.libraryState.value.toMoviesState()
        )

    private fun RadarrLibraryState.toMoviesState(): MoviesState = MoviesState(
        isLoading = isLoading,
        needsConfiguration = needsConfiguration,
        error = error,
        recommendedMovies = recommendedMovies,
        trendingMovies = trendingMovies,
        popularMovies = popularMovies,
        rootFolders = rootFolders,
    ).withMovies(movies)
}