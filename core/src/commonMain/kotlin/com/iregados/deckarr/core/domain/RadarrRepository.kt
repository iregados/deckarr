package com.iregados.deckarr.core.domain

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.AddOptions
import com.iregados.api.radarr.dto.Movie
import com.iregados.api.radarr.dto.RadarrApiConfig
import com.iregados.api.radarr.dto.RadarrQualityProfile
import com.iregados.api.radarr.dto.RadarrRelease
import com.iregados.api.radarr.dto.RadarrRootFolder
import com.iregados.api.radarr.methods.addMovie
import com.iregados.api.radarr.methods.addRelease
import com.iregados.api.radarr.methods.findMovieByTmdbId
import com.iregados.api.radarr.methods.getAddedMovieByTmdbId
import com.iregados.api.radarr.methods.getImportListMovies
import com.iregados.api.radarr.methods.getMovieByRadarrId
import com.iregados.api.radarr.methods.getMovies
import com.iregados.api.radarr.methods.getQualityProfiles
import com.iregados.api.radarr.methods.getReleasesByRadarrId
import com.iregados.api.radarr.methods.getRootFolders
import com.iregados.api.radarr.methods.lookupMovies
import com.iregados.api.radarr.methods.removeMovieById
import com.iregados.api.radarr.methods.systemStatus
import com.iregados.deckarr.core.domain.dto.ImportListMoviesResponse
import io.ktor.http.URLProtocol
import kotlinx.coroutines.flow.StateFlow

class RadarrRepository(
    private val radarrApi: RadarrApi
) {

    fun getConfigFlow(): StateFlow<RadarrApiConfig> = radarrApi.configFlow

    suspend fun systemStatus(
        connectionAddress: String,
        apiKey: String
    ): Boolean? {
        val tempApi = RadarrApi(
            if (connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
            connectionAddress.drop(if (connectionAddress.startsWith("https")) 8 else 7),
            apiKey
        )
        return when (val res = tempApi.systemStatus()) {
            is ApiResult.Success -> true
            is ApiResult.Error -> null
        }
    }

    suspend fun getMovies(): List<Movie>? {
        return when (val res = radarrApi.getMovies()) {
            is ApiResult.Success -> {
                res.data.sortedByDescending {
                    it.added
                }
            }

            is ApiResult.Error -> null
        }
    }

    suspend fun getImportListMovies(
        includeRecommendations: Boolean,
        includeTrending: Boolean,
        includePopular: Boolean
    ): ImportListMoviesResponse? {
        return when (val res = radarrApi.getImportListMovies(
            includeRecommendations,
            includeTrending,
            includePopular
        )) {
            is ApiResult.Success -> {
                ImportListMoviesResponse(
                    recommendations = res.data
                        .filter { it.isRecommendation ?: false }
                        .sortedByDescending {
                            it.ratings?.imdb?.value
                        },
                    popular = res.data
                        .filter { it.isPopular ?: false }
                        .sortedByDescending {
                            it.ratings?.imdb?.value
                        },
                    trending = res.data
                        .filter { it.isTrending ?: false }
                        .sortedByDescending {
                            it.ratings?.imdb?.value
                        },
                )
            }

            is ApiResult.Error -> null

        }
    }

    suspend fun getMovieByRadarrId(id: Long): Movie? {
        return when (val res = radarrApi.getMovieByRadarrId(id)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun findMovieByTmdbId(id: Long): Movie? {
        return when (val res = radarrApi.findMovieByTmdbId(id)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getAddedMovieByTmdbId(id: Long): Movie? {
        return when (val res = radarrApi.getAddedMovieByTmdbId(id)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun removeMovieById(
        movieId: Long,
        deleteFiles: Boolean,
        addImportExclusion: Boolean
    ): Boolean? {
        return when (val res = radarrApi.removeMovieById(
            movieId,
            deleteFiles,
            addImportExclusion
        )) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun addMovie(
        movie: Movie,
        qualityProfileId: Long,
        rootFolderPath: String,
        shouldMonitor: Boolean
    ): Movie? {
        return when (val res = radarrApi.addMovie(
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
        )) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getQualityProfiles(): List<RadarrQualityProfile>? {
        return when (val res = radarrApi.getQualityProfiles()) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getRootFolders(): List<RadarrRootFolder>? {
        return when (val res = radarrApi.getRootFolders()) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getReleases(movieId: Long): List<RadarrRelease>? {
        return when (val res = radarrApi.getReleasesByRadarrId(movieId)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun addRelease(
        radarrRelease: RadarrRelease
    ): RadarrRelease? {
        return when (val res = radarrApi.addRelease(
            radarrRelease
        )) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun lookupMovies(query: String): List<Movie>? {
        return when (val result = radarrApi.lookupMovies(query)) {
            is ApiResult.Success -> result.data
            is ApiResult.Error -> null
        }
    }
}