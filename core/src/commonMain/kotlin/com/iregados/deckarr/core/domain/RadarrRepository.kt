package com.iregados.deckarr.core.domain

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.api.radarr.dto.RadarrApiConfig
import com.iregados.deckarr.api.radarr.dto.RadarrQualityProfile
import com.iregados.deckarr.api.radarr.dto.RadarrRelease
import com.iregados.deckarr.api.radarr.dto.RadarrRootFolder
import com.iregados.deckarr.core.domain.dto.ImportListMoviesResponse
import com.iregados.deckarr.core.domain.dto.RadarrLibraryState
import com.iregados.deckarr.core.util.dto.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface RadarrRepository {
    val libraryState: StateFlow<RadarrLibraryState>
    val notificationFlow: SharedFlow<Notification>

    fun getConfigFlow(): Flow<RadarrApiConfig>

    suspend fun refresh(): ApiResult<Boolean>

    suspend fun systemStatus(
        connectionAddress: String,
        apiKey: String
    ): ApiResult<Boolean>

    suspend fun getMovies(): ApiResult<List<Movie>>

    suspend fun getImportListMovies(
        includeRecommendations: Boolean,
        includeTrending: Boolean,
        includePopular: Boolean
    ): ApiResult<ImportListMoviesResponse>

    suspend fun getMovieByRadarrId(id: Long): ApiResult<Movie>

    suspend fun findMovieByTmdbId(id: Long): ApiResult<Movie>

    suspend fun getAddedMovieByTmdbId(id: Long): ApiResult<Movie>

    suspend fun removeMovieById(
        movieId: Long,
        deleteFiles: Boolean,
        addImportExclusion: Boolean
    ): ApiResult<Boolean>

    suspend fun addMovie(
        movie: Movie,
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean
    ): ApiResult<Movie>

    suspend fun getQualityProfiles(): ApiResult<List<RadarrQualityProfile>>

    suspend fun getRootFolders(): ApiResult<List<RadarrRootFolder>>

    suspend fun getReleases(movieId: Long): ApiResult<List<RadarrRelease>>

    suspend fun addRelease(radarrRelease: RadarrRelease): ApiResult<RadarrRelease>

    suspend fun lookupMovies(query: String): ApiResult<List<Movie>>
}