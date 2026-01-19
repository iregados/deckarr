package com.iregados.deckarr.feature.movies.movie_detail

import com.iregados.api.radarr.dto.Movie
import com.iregados.api.radarr.dto.RadarrQualityProfile
import com.iregados.api.radarr.dto.RadarrRootFolder

data class MovieDetailState(
    val isLoading: Boolean = true,
    val isAdding: Boolean = false,
    val isRemoving: Boolean = false,
    val error: String? = null,
    val movie: Movie? = null,
    val radarrQualityProfiles: List<RadarrQualityProfile>? = null,
    val radarrRootFolders: List<RadarrRootFolder>? = null,
    val selectedQualityProfileId: Long? = null,
    val selectedRootFolderPath: String? = null,
)

