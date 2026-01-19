package com.iregados.deckarr.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.domain.SonarrRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    val radarrRepository: RadarrRepository,
    val sonarrRepository: SonarrRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _uiState.value
        )

    private var searchJob: Job? = null

    init {
        sonarrRepository.getConfigFlow()
            .onEach {
                _uiState.update { state ->
                    state.copy(
                        sonarrNeedsConfiguration = it.host.isEmpty() || it.apiKey.isEmpty()
                    )
                }

            }
            .launchIn(viewModelScope)

        radarrRepository.getConfigFlow()
            .onEach {
                _uiState.update { state ->
                    state.copy(
                        radarrNeedsConfiguration = it.host.isEmpty() || it.apiKey.isEmpty()
                    )
                }

            }
            .launchIn(viewModelScope)
    }

    fun onEvent(
        event: SearchEvent
    ) {
        when (event) {
            is SearchEvent.SetQuery -> {
                _uiState.update {
                    it.copy(
                        query = event.query
                    )
                }
                if (event.query.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            movies = null,
                            series = null
                        )
                    }
                } else {

                    when (_uiState.value.searchType) {
                        SearchType.Movies -> searchMovies(event.query)
                        SearchType.Series -> searchSeries(event.query)
                    }
                }
            }

            is SearchEvent.SetSearchType -> {
                _uiState.update {
                    it.copy(
                        searchType = event.searchType
                    )
                }
                if (_uiState.value.query.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            movies = null,
                            series = null
                        )
                    }
                } else {
                    when (event.searchType) {
                        SearchType.Movies -> searchMovies(_uiState.value.query)
                        SearchType.Series -> searchSeries(_uiState.value.query)
                    }
                }
            }
        }
    }

    fun searchMovies(
        query: String
    ) {
        searchJob?.cancel()
        if (_uiState.value.radarrNeedsConfiguration) return

        searchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            val movies = radarrRepository.lookupMovies(query)
            _uiState.value = _uiState.value.copy(
                movies = movies,
                isLoading = false
            )
        }
    }

    fun searchSeries(
        query: String
    ) {
        searchJob?.cancel()
        if (_uiState.value.sonarrNeedsConfiguration) return

        searchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            val series = sonarrRepository.lookupSeries(query)
            _uiState.value = _uiState.value.copy(
                series = series,
                isLoading = false
            )
        }
    }
}
