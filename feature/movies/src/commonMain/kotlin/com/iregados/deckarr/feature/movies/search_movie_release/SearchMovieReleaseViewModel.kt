package com.iregados.deckarr.feature.movies.search_movie_release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.api.radarr.dto.RadarrRelease
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.util.dto.ReleaseSortOption
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
                    error = null,
                )
            }

            val movieId = passedMovie.id
            if (movieId == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "This movie is not added to Radarr yet",
                        radarrReleases = emptyList(),
                    )
                }
                return@launch
            }

            val result = radarrRepository.getReleases(movieId)
            val torrentReleases = result.getOrNull()
                ?.filter { it.protocol == "torrent" }

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    radarrReleases = torrentReleases?.let {
                        sortReleases(it, state.sortOption, state.sortAscending)
                    },
                    error = (result as? ApiResult.Error)?.message,
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
            ).getOrNull()?.let {
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

    fun onSortSelected(option: ReleaseSortOption) {
        _uiState.update { state ->
            val ascending = if (state.sortOption == option) {
                !state.sortAscending
            } else {
                option.defaultAscending
            }
            state.copy(
                sortOption = option,
                sortAscending = ascending,
                radarrReleases = state.radarrReleases?.let { sortReleases(it, option, ascending) },
            )
        }
    }

    private fun sortReleases(
        releases: List<RadarrRelease>,
        option: ReleaseSortOption,
        ascending: Boolean,
    ): List<RadarrRelease> {
        val primary: Comparator<RadarrRelease> = when (option) {
            ReleaseSortOption.APPROVED -> compareBy { it.approved }
            ReleaseSortOption.SEEDERS -> compareBy { it.seeders }
            ReleaseSortOption.SIZE -> compareBy { it.size }
            ReleaseSortOption.QUALITY -> compareBy<RadarrRelease> { it.quality.quality.resolution }
                .thenBy { it.qualityWeight }

            ReleaseSortOption.INDEXER -> compareBy { it.indexer.lowercase() }
            ReleaseSortOption.AGE -> compareBy { it.ageMinutes }
        }
        val ordered = if (ascending) primary else primary.reversed()
        return releases.sortedWith(
            ordered.thenBy { it.size }.thenBy { it.indexer.lowercase() }
        )
    }
}