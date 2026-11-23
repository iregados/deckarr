package com.iregados.deckarr.core.domain

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.core.domain.dto.SonarrLibraryState
import com.iregados.deckarr.core.util.dto.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SonarrRepository {
    val libraryState: StateFlow<SonarrLibraryState>
    val notificationFlow: SharedFlow<Notification>

    fun getConfigFlow(): Flow<com.iregados.deckarr.api.sonarr.dto.SonarrApiConfig>

    suspend fun refresh(): ApiResult<Boolean>

    suspend fun testConnection(
        connectionAddress: String,
        apiKey: String
    ): ApiResult<Boolean>

    suspend fun getSeries(): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.Series>>

    suspend fun addSeries(
        series: com.iregados.deckarr.api.sonarr.dto.Series,
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean
    ): ApiResult<com.iregados.deckarr.api.sonarr.dto.Series>

    suspend fun removeSeriesById(
        seriesId: Long,
        deleteFiles: Boolean,
        addImportExclusion: Boolean
    ): ApiResult<Boolean>

    suspend fun getSeriesBySonarrId(id: Long): ApiResult<com.iregados.deckarr.api.sonarr.dto.Series>

    suspend fun getSeriesByTvdbId(id: Long): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.Series>>

    suspend fun getEpisodesBySonarrId(seriesId: Long): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.Episode>>

    suspend fun getReleases(episodeId: Long): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.SonarrRelease>>

    suspend fun getReleases(
        seriesId: Long,
        seasonNumber: Long
    ): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.SonarrRelease>>

    suspend fun addRelease(sonarrRelease: com.iregados.deckarr.api.sonarr.dto.SonarrRelease): ApiResult<com.iregados.deckarr.api.sonarr.dto.SonarrRelease>

    suspend fun getQualityProfiles(): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.SonarrQualityProfile>>

    suspend fun getRootFolders(): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.SonarrRootFolder>>

    suspend fun lookupSeries(query: String): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.Series>>
}