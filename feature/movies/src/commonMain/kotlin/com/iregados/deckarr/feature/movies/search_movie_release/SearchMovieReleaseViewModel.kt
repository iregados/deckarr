package com.iregados.deckarr.feature.movies.search_movie_release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.api.radarr.dto.Movie
import com.iregados.api.radarr.dto.RadarrRelease
import com.iregados.deckarr.core.domain.RadarrRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchMovieReleaseViewModel(
    private val radarrRepository: RadarrRepository,
    private val passedMovie: Movie
) : ViewModel() {
    private val _uiState: MutableStateFlow<SearchMovieReleaseState> =
        MutableStateFlow(SearchMovieReleaseState())
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _uiState.value
        )

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

            val releases = radarrRepository.getReleases(passedMovie.id!!)
                ?.filter { it.protocol == "torrent" }
                ?.sortedBy { it.indexer }
                ?.sortedBy { it.size }
                ?.sortedByDescending { it.approved }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    radarrReleases = releases,
                )
            }
        }
    }

    fun addReleaseToMovie(
        radarrRelease: RadarrRelease,
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    grabbingReleasesIds = it.grabbingReleasesIds + radarrRelease.guid,
                )
            }

            radarrRepository.addRelease(
                radarrRelease = radarrRelease
            )?.let {
                _uiState.update {
                    it.copy(
                        grabbingReleasesIds = it.grabbingReleasesIds - radarrRelease.guid,
                        grabbedReleasesIds = it.grabbedReleasesIds + radarrRelease.guid,
                    )
                }
            } ?: run {
                _uiState.update {
                    it.copy(
                        grabbingReleasesIds = it.grabbingReleasesIds - radarrRelease.guid,
                        errorWhileGrabbingReleasesIds = it.errorWhileGrabbingReleasesIds + radarrRelease.guid,
                    )
                }
            }
        }
    }
}