package com.iregados.deckarr.feature.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.movies.movie_detail.navigation.keys.MovieDetailKey
import com.iregados.deckarr.feature.search.components.MoviesListResult
import com.iregados.deckarr.feature.search.components.SearchTopSelector
import com.iregados.deckarr.feature.search.components.SeriesListResult
import com.iregados.deckarr.feature.series.series_detail.navigation.keys.SeriesDetailKey
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    popMainBackStack: () -> Unit,
    navigateMainTo: (NavKey) -> Unit,
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

    val searchBarState = rememberSearchBarState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopSearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                state = searchBarState,
                inputField = {
                    SearchBarDefaults.InputField(
                        modifier = Modifier.focusRequester(focusRequester),
                        query = uiState.query,
                        onQueryChange = {
                            searchViewModel.onEvent(
                                SearchEvent.SetQuery(it)
                            )
                        },
                        onSearch = {
                            searchViewModel.onEvent(
                                SearchEvent.SetQuery(it)
                            )
                        },
                        expanded = false,
                        onExpandedChange = {},
                        placeholder = { Text("Search...") },
                        leadingIcon = {
                            IconButton(
                                onClick = { popMainBackStack() }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        trailingIcon = {
                            if (!uiState.query.isEmpty()) {
                                IconButton(
                                    onClick = { searchViewModel.onEvent(SearchEvent.SetQuery("")) }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        },
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
        ) {
            SearchTopSelector(
                searchType = uiState.searchType,
                onSelect = {
                    searchViewModel.onEvent(SearchEvent.SetSearchType(it))
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (uiState.query == "") return@Box

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                    return@Box
                }

                if (uiState.searchType == SearchType.Movies) {
                    MoviesListResult(
                        movies = uiState.movies,
                        needsConfiguration = uiState.radarrNeedsConfiguration,
                        onSelect = {
                            navigateMainTo(
                                MovieDetailKey(it)
                            )
                        }
                    )
                } else {
                    SeriesListResult(
                        series = uiState.series,
                        needsConfiguration = uiState.sonarrNeedsConfiguration,
                        onSelect = {
                            navigateMainTo(
                                SeriesDetailKey(it)
                            )
                        }
                    )
                }
            }
        }
    }
}