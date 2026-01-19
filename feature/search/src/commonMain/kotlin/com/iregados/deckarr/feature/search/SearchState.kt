package com.iregados.deckarr.feature.search

import com.iregados.api.radarr.dto.Movie
import com.iregados.api.sonarr.dto.Series

data class SearchState(
    val isLoading: Boolean = false,
    val radarrNeedsConfiguration: Boolean = false,
    val sonarrNeedsConfiguration: Boolean = false,
    val error: String? = null,
    val query: String = "",
    val searchType: SearchType = SearchType.Movies,
    val movies: List<Movie>? = null,
    val series: List<Series>? = null,
)

