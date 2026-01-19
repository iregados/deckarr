package com.iregados.deckarr.core.domain

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.sonarr.dto.AddOptions
import com.iregados.api.sonarr.dto.Episode
import com.iregados.api.sonarr.dto.Series
import com.iregados.api.sonarr.dto.SonarrApiConfig
import com.iregados.api.sonarr.dto.SonarrQualityProfile
import com.iregados.api.sonarr.dto.SonarrRelease
import com.iregados.api.sonarr.dto.SonarrRootFolder
import com.iregados.api.sonarr.methods.addRelease
import com.iregados.api.sonarr.methods.addSeries
import com.iregados.api.sonarr.methods.getEpisodesBySonarrId
import com.iregados.api.sonarr.methods.getQualityProfiles
import com.iregados.api.sonarr.methods.getReleases
import com.iregados.api.sonarr.methods.getRootFolders
import com.iregados.api.sonarr.methods.getSeries
import com.iregados.api.sonarr.methods.getSeriesBySonarrId
import com.iregados.api.sonarr.methods.getSeriesByTvdbId
import com.iregados.api.sonarr.methods.lookupSeries
import com.iregados.api.sonarr.methods.removeSeriesById
import com.iregados.api.sonarr.methods.systemStatus
import io.ktor.http.URLProtocol
import kotlinx.coroutines.flow.StateFlow

class SonarrRepository(
    private val sonarrApi: SonarrApi
) {
    fun getConfigFlow(): StateFlow<SonarrApiConfig> = sonarrApi.configFlow

    suspend fun testConnection(
        connectionAddress: String,
        apiKey: String
    ): Boolean? {
        val tempApi = SonarrApi(
            if (connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
            connectionAddress.drop(if (connectionAddress.startsWith("https")) 8 else 7),
            apiKey
        )
        return when (val res = tempApi.systemStatus()) {
            is ApiResult.Success -> true
            is ApiResult.Error -> null

        }
    }

    suspend fun getSeries(): List<Series>? {
        return when (val res = sonarrApi.getSeries()) {
            is ApiResult.Success -> {
                res.data.sortedByDescending {
                    it.added
                }
            }

            is ApiResult.Error -> null
        }
    }

    suspend fun addSeries(
        series: Series,
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean
    ): Series? {
        return when (val res = sonarrApi.addSeries(
            series.copy(
                qualityProfileId = qualityProfileId,
                rootFolderPath = rootFolderPath,
                addOptions = AddOptions(
                    monitor = if (shouldMonitor) "all" else "none",
                    searchForMissingEpisodes = false,
                    searchForCutoffUnmetEpisodes = false
                ),
                monitored = shouldMonitor,
                seasons = series.seasons.map {
                    it.copy(
                        monitored = shouldMonitor
                    )
                }
            ),
        )) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun removeSeriesById(
        seriesId: Long,
        deleteFiles: Boolean,
        addImportExclusion: Boolean
    ): Boolean? {
        return when (val res = sonarrApi.removeSeriesById(
            seriesId,
            deleteFiles,
            addImportExclusion
        )) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getSeriesBySonarrId(id: Long): Series? {
        return when (val res = sonarrApi.getSeriesBySonarrId(id)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getSeriesByTvdbId(id: Long): List<Series>? {
        return when (val res = sonarrApi.getSeriesByTvdbId(id)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getEpisodesBySonarrId(
        seriesId: Long
    ): List<Episode>? {
        return when (val res = sonarrApi.getEpisodesBySonarrId(seriesId)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }


    suspend fun getReleases(
        episodeId: Long
    ): List<SonarrRelease>? {
        return when (val res = sonarrApi.getReleases(episodeId)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getReleases(
        seriesId: Long,
        seasonNumber: Long
    ): List<SonarrRelease>? {
        return when (val res = sonarrApi.getReleases(seriesId, seasonNumber)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun addRelease(
        sonarrRelease: SonarrRelease
    ): SonarrRelease? {
        return when (val res = sonarrApi.addRelease(
            sonarrRelease
        )) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getQualityProfiles(): List<SonarrQualityProfile>? {
        return when (val res = sonarrApi.getQualityProfiles()) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getRootFolders(): List<SonarrRootFolder>? {
        return when (val res = sonarrApi.getRootFolders()) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun lookupSeries(query: String): List<Series>? {
        return when (val result = sonarrApi.lookupSeries(query)) {
            is ApiResult.Success -> result.data
            is ApiResult.Error -> null
        }
    }
}