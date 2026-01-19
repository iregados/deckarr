package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.Movie

suspend fun RadarrApi.getAddedMovieByTmdbId(id: Long): ApiResult<Movie> {
    val response = apiGet<
            List<Movie>
            >("/api/v3/movie?tmdbId=$id")
    return when (response) {
        is ApiResult.Success -> {
            response.data.firstOrNull()?.let {
                ApiResult.Success(it)
            } ?: run {
                ApiResult.Error("Movie not found")
            }
        }

        is ApiResult.Error -> {
            response
        }
    }
}
