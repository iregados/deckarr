package com.iregados.deckarr.core.data.repository

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.util.ConnectionAddress
import com.iregados.deckarr.api.common.util.map
import com.iregados.deckarr.api.sonarr.SonarrApi
import com.iregados.deckarr.api.sonarr.dto.AddOptions
import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.api.sonarr.dto.SonarrRelease
import com.iregados.deckarr.api.sonarr.methods.addRelease
import com.iregados.deckarr.api.sonarr.methods.addSeries
import com.iregados.deckarr.api.sonarr.methods.getEpisodesBySonarrId
import com.iregados.deckarr.api.sonarr.methods.getQualityProfiles
import com.iregados.deckarr.api.sonarr.methods.getReleases
import com.iregados.deckarr.api.sonarr.methods.getRootFolders
import com.iregados.deckarr.api.sonarr.methods.getSeries
import com.iregados.deckarr.api.sonarr.methods.getSeriesBySonarrId
import com.iregados.deckarr.api.sonarr.methods.getSeriesByTvdbId
import com.iregados.deckarr.api.sonarr.methods.lookupSeries
import com.iregados.deckarr.api.sonarr.methods.removeSeriesById
import com.iregados.deckarr.api.sonarr.methods.systemStatus
import com.iregados.deckarr.api.tmdb.TmdbApi
import com.iregados.deckarr.api.tmdb.methods.getPopularSeries
import com.iregados.deckarr.api.tmdb.methods.getSeriesRecommendations
import com.iregados.deckarr.api.tmdb.methods.getTrendingSeries
import com.iregados.deckarr.core.domain.SonarrRepository
import com.iregados.deckarr.core.domain.dto.SonarrLibraryState
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import com.iregados.deckarr.core.util.extension.mergeSeriesRecommendations
import com.iregados.deckarr.core.util.extension.toSonarrSeries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SonarrRepositoryImpl(
    private val sonarrApi: SonarrApi,
    private val tmdbApi: TmdbApi,
    private val scope: CoroutineScope
) : SonarrRepository {
    private val _libraryState = MutableStateFlow(SonarrLibraryState())
    override val libraryState = _libraryState.asStateFlow()

    private val _notificationFlow = MutableSharedFlow<Notification>(
        replay = 0,
        extraBufferCapacity = 4
    )
    override val notificationFlow = _notificationFlow.asSharedFlow()

    init {
        scope.launch {
            sonarrApi.configFlow.collectLatest { apiConfig ->
                if (apiConfig.host.isEmpty() || apiConfig.apiKey.isEmpty()) {
                    _libraryState.update {
                        SonarrLibraryState(
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

    override fun getConfigFlow() = sonarrApi.configFlow

    override suspend fun refresh(): ApiResult<Boolean> {
        if (_libraryState.value.needsConfiguration) {
            return ApiResult.Success(false)
        }

        _libraryState.update { it.copy(isLoading = true, error = null) }

        return coroutineScope {
            val seriesDeferred = async { getSeries() }
            val rootFoldersDeferred = async { getRootFolders() }
            val popularDeferred = async { tmdbApi.getPopularSeries() }
            val trendingDeferred = async { tmdbApi.getTrendingSeries() }

            val seriesResult = seriesDeferred.await()
            val rootFoldersResult = rootFoldersDeferred.await()
            val popularResult = popularDeferred.await()
            val trendingResult = trendingDeferred.await()

            val librarySeries = seriesResult.getOrNull() ?: _libraryState.value.series
            val libraryByTmdbId = librarySeries
                .orEmpty()
                .filter { it.tmdbId != 0L }
                .associateBy { it.tmdbId }

            val seedIds = librarySeries
                .orEmpty()
                .filter { it.tmdbId != 0L }
                .sortedByDescending { it.added }
                .take(10)
                .map { it.tmdbId }

            val recommendationResults = seedIds
                .map { tmdbId -> async { tmdbApi.getSeriesRecommendations(tmdbId) } }
                .awaitAll()

            val successfulRecommendations = recommendationResults
                .mapNotNull { it.getOrNull()?.results }

            val recommendedSeries = when {
                librarySeries == null -> null
                seedIds.isEmpty() -> emptyList()
                successfulRecommendations.isNotEmpty() ->
                    successfulRecommendations.mergeSeriesRecommendations(libraryByTmdbId.keys)

                else -> null
            }

            val popularSeries = popularResult.getOrNull()?.results?.map { tmdbSeries ->
                libraryByTmdbId[tmdbSeries.id] ?: tmdbSeries.toSonarrSeries()
            }
            val trendingSeries = trendingResult.getOrNull()?.results?.map { tmdbSeries ->
                libraryByTmdbId[tmdbSeries.id] ?: tmdbSeries.toSonarrSeries()
            }

            _libraryState.update { state ->
                state.copy(
                    series = seriesResult.getOrNull() ?: state.series,
                    recommendedSeries = recommendedSeries ?: state.recommendedSeries,
                    popularSeries = popularSeries ?: state.popularSeries,
                    trendingSeries = trendingSeries ?: state.trendingSeries,
                    rootFolders = rootFoldersResult.getOrNull() ?: state.rootFolders,
                    isLoading = false,
                    error = (seriesResult as? ApiResult.Error)?.message
                )
            }

            val seriesError = seriesResult as? ApiResult.Error
            if (seriesError != null && _libraryState.value.series != null) {
                _notificationFlow.emitError("Error loading series: ${seriesError.message}")
            }

            val rootFoldersError = rootFoldersResult as? ApiResult.Error
            if (rootFoldersError != null) {
                _notificationFlow.emitError(
                    "Error loading root folders: ${rootFoldersError.message}"
                )
            }

            val recommendationError =
                if (seedIds.isNotEmpty() && successfulRecommendations.isEmpty()) {
                    recommendationResults.firstNotNullOfOrNull { it as? ApiResult.Error }
                } else {
                    null
                }

            val tmdbError = (popularResult as? ApiResult.Error)
                ?: (trendingResult as? ApiResult.Error)
                ?: recommendationError
            if (tmdbError != null) {
                _notificationFlow.emitError(
                    "Error loading TMDB series data: ${tmdbError.message}"
                )
            }

            when (seriesResult) {
                is ApiResult.Success -> ApiResult.Success(true)
                is ApiResult.Error -> seriesResult
            }
        }
    }

    override suspend fun testConnection(
        connectionAddress: String,
        apiKey: String
    ): ApiResult<Boolean> {
        val address = ConnectionAddress.parse(connectionAddress)
        return SonarrApi(
            address.protocol,
            address.host,
            apiKey,
            address.path
        ).use { tempApi ->
            tempApi.systemStatus().map { true }
        }
    }

    override suspend fun addSeries(
        series: Series,
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean
    ): ApiResult<Series> {
        val result = sonarrApi.addSeries(
            series.copy(
                qualityProfileId = qualityProfileId,
                rootFolderPath = rootFolderPath,
                addOptions = AddOptions(
                    monitor = if (shouldMonitor) "all" else "none",
                    searchForMissingEpisodes = false,
                    searchForCutoffUnmetEpisodes = false
                ),
                monitored = shouldMonitor,
                seasons = series.seasons.map { it.copy(monitored = shouldMonitor) }
            ),
        )
        if (result is ApiResult.Success) {
            _libraryState.update { state ->
                val updatedSeries = ((state.series ?: emptyList())
                    .filterNot { it.tvdbId == result.data.tvdbId || it.id == result.data.id } + result.data)
                    .sortedByDescending { it.added }
                state.copy(series = updatedSeries)
            }
        }
        return result
    }

    override suspend fun getSeries() =
        sonarrApi.getSeries().map { series -> series.sortedByDescending { it.added } }

    override suspend fun removeSeriesById(
        seriesId: Long,
        deleteFiles: Boolean,
        addImportExclusion: Boolean
    ): ApiResult<Boolean> {
        val result = sonarrApi.removeSeriesById(seriesId, deleteFiles, addImportExclusion)
        if (result is ApiResult.Success && result.data) {
            _libraryState.update { state ->
                state.copy(
                    series = state.series?.filter { it.id != seriesId }
                )
            }
        }
        return result
    }

    override suspend fun getSeriesBySonarrId(id: Long) = sonarrApi.getSeriesBySonarrId(id)

    override suspend fun getSeriesByTvdbId(id: Long) = sonarrApi.getSeriesByTvdbId(id)

    override suspend fun getEpisodesBySonarrId(
        seriesId: Long
    ) = sonarrApi.getEpisodesBySonarrId(seriesId)

    override suspend fun getReleases(
        episodeId: Long
    ) = sonarrApi.getReleases(episodeId)

    override suspend fun getReleases(
        seriesId: Long,
        seasonNumber: Long
    ) = sonarrApi.getReleases(seriesId, seasonNumber)

    override suspend fun addRelease(
        sonarrRelease: SonarrRelease
    ) = sonarrApi.addRelease(sonarrRelease)

    override suspend fun getQualityProfiles() = sonarrApi.getQualityProfiles()

    override suspend fun getRootFolders() = sonarrApi.getRootFolders()

    override suspend fun lookupSeries(query: String) = sonarrApi.lookupSeries(query)
}