package com.iregados.deckarr.core.data.repository

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.util.ConnectionAddress
import com.iregados.deckarr.api.common.util.map
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.radarr.dto.AddOptions
import com.iregados.deckarr.api.radarr.dto.Movie
import com.iregados.deckarr.api.radarr.dto.RadarrRelease
import com.iregados.deckarr.api.radarr.methods.addMovie
import com.iregados.deckarr.api.radarr.methods.addRelease
import com.iregados.deckarr.api.radarr.methods.findMovieByTmdbId
import com.iregados.deckarr.api.radarr.methods.getAddedMovieByTmdbId
import com.iregados.deckarr.api.radarr.methods.getImportListMovies
import com.iregados.deckarr.api.radarr.methods.getMovieByRadarrId
import com.iregados.deckarr.api.radarr.methods.getMovies
import com.iregados.deckarr.api.radarr.methods.getQualityProfiles
import com.iregados.deckarr.api.radarr.methods.getReleasesByRadarrId
import com.iregados.deckarr.api.radarr.methods.getRootFolders
import com.iregados.deckarr.api.radarr.methods.lookupMovies
import com.iregados.deckarr.api.radarr.methods.removeMovieById
import com.iregados.deckarr.api.radarr.methods.systemStatus
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.domain.dto.ImportListMoviesResponse
import com.iregados.deckarr.core.domain.dto.RadarrLibraryState
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RadarrRepositoryImpl(
    private val radarrApi: RadarrApi,
    private val scope: CoroutineScope
) : RadarrRepository {
    private val _libraryState = MutableStateFlow(RadarrLibraryState())
    override val libraryState = _libraryState.asStateFlow()

    private val _notificationFlow = MutableSharedFlow<Notification>(
        replay = 0,
        extraBufferCapacity = 4
    )
    override val notificationFlow = _notificationFlow.asSharedFlow()

    init {
        scope.launch {
            radarrApi.configFlow.collectLatest { apiConfig ->
                if (apiConfig.host.isEmpty() || apiConfig.apiKey.isEmpty()) {
                    _libraryState.update {
                        RadarrLibraryState(
                            isLoading = false,
                            isConfigLoaded = true,
                            needsConfiguration = true,
                        )
                    }
                } else {
                    _libraryState.update {
                        it.copy(
                            isConfigLoaded = true,
                            needsConfiguration = false,
                        )
                    }
                    refresh()
                }
            }
        }
    }

    override fun getConfigFlow() = radarrApi.configFlow

    override suspend fun refresh(): ApiResult<Boolean> {
        if (_libraryState.value.needsConfiguration) {
            return ApiResult.Success(false)
        }

        _libraryState.update { it.copy(isLoading = true, error = null) }

        return coroutineScope {
            val moviesDeferred = async { getMovies() }
            val importListDeferred = async {
                getImportListMovies(
                    includeRecommendations = true,
                    includeTrending = true,
                    includePopular = true
                )
            }
            val rootFoldersDeferred = async { getRootFolders() }

            val moviesResult = moviesDeferred.await()
            val importListResult = importListDeferred.await()
            val rootFoldersResult = rootFoldersDeferred.await()

            val importList = importListResult.getOrNull()

            _libraryState.update { state ->
                state.copy(
                    movies = moviesResult.getOrNull() ?: state.movies,
                    recommendedMovies = importList?.recommendations ?: state.recommendedMovies,
                    trendingMovies = importList?.trending ?: state.trendingMovies,
                    popularMovies = importList?.popular ?: state.popularMovies,
                    rootFolders = rootFoldersResult.getOrNull() ?: state.rootFolders,
                    isLoading = false,
                    error = (moviesResult as? ApiResult.Error)?.message
                )
            }

            val moviesError = moviesResult as? ApiResult.Error
            if (moviesError != null && _libraryState.value.movies != null) {
                _notificationFlow.emitError("Error loading movies: ${moviesError.message}")
            }

            val importListError = importListResult as? ApiResult.Error
            if (importListError != null) {
                _notificationFlow.emitError(
                    "Error loading recommendations: ${importListError.message}"
                )
            }

            when (moviesResult) {
                is ApiResult.Success -> ApiResult.Success(true)
                is ApiResult.Error -> moviesResult
            }
        }
    }

    override suspend fun systemStatus(
        connectionAddress: String,
        apiKey: String
    ): ApiResult<Boolean> {
        val address = ConnectionAddress.parse(connectionAddress)
        return RadarrApi(
            address.protocol,
            address.host,
            apiKey,
            address.path
        ).use { tempApi ->
            tempApi.systemStatus().map { true }
        }
    }

    override suspend fun getMovies(): ApiResult<List<Movie>> =
        radarrApi.getMovies().map { movies -> movies.sortedByDescending { it.added } }

    override suspend fun getImportListMovies(
        includeRecommendations: Boolean,
        includeTrending: Boolean,
        includePopular: Boolean
    ) = radarrApi.getImportListMovies(
        includeRecommendations,
        includeTrending,
        includePopular
    ).map { movies ->
        ImportListMoviesResponse(
            recommendations = movies
                .filter { it.isRecommendation ?: false }
                .sortedByDescending { it.ratings?.imdb?.value },
            popular = movies
                .filter { it.isPopular ?: false }
                .sortedByDescending { it.ratings?.imdb?.value },
            trending = movies
                .filter { it.isTrending ?: false }
                .sortedByDescending { it.ratings?.imdb?.value },
        )
    }

    override suspend fun getMovieByRadarrId(id: Long) = radarrApi.getMovieByRadarrId(id)

    override suspend fun findMovieByTmdbId(id: Long) = radarrApi.findMovieByTmdbId(id)

    override suspend fun getAddedMovieByTmdbId(id: Long) = radarrApi.getAddedMovieByTmdbId(id)

    override suspend fun removeMovieById(
        movieId: Long,
        deleteFiles: Boolean,
        addImportExclusion: Boolean
    ): ApiResult<Boolean> {
        val result = radarrApi.removeMovieById(movieId, deleteFiles, addImportExclusion)
        if (result is ApiResult.Success && result.data) {
            _libraryState.update { state ->
                state.copy(
                    movies = state.movies?.filter { it.id != movieId }
                )
            }
        }
        return result
    }

    override suspend fun addMovie(
        movie: Movie,
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean
    ): ApiResult<Movie> {
        val result = radarrApi.addMovie(
            movie.copy(
                qualityProfileId = qualityProfileId,
                rootFolderPath = rootFolderPath,
                addOptions = AddOptions(
                    monitor = if (shouldMonitor) "movieOnly" else "none",
                    searchForMovie = false
                ),
                monitored = shouldMonitor,
                minimumAvailability = "released"
            ),
        )
        if (result is ApiResult.Success) {
            _libraryState.update { state ->
                val updatedMovies = ((state.movies ?: emptyList())
                    .filterNot { it.tmdbId == result.data.tmdbId || it.id == result.data.id } + result.data)
                    .sortedByDescending { it.added }
                state.copy(movies = updatedMovies)
            }
        }
        return result
    }

    override suspend fun getQualityProfiles() = radarrApi.getQualityProfiles()

    override suspend fun getRootFolders() = radarrApi.getRootFolders()

    override suspend fun getReleases(movieId: Long) = radarrApi.getReleasesByRadarrId(movieId)

    override suspend fun addRelease(
        radarrRelease: RadarrRelease
    ) = radarrApi.addRelease(radarrRelease)

    override suspend fun lookupMovies(query: String) = radarrApi.lookupMovies(query)
}