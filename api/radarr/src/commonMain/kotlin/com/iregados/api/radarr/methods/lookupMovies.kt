package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.Movie

suspend fun RadarrApi.lookupMovies(query: String): ApiResult<List<Movie>> {
    val response = apiGet<
            List<Movie>
            >("/api/v3/movie/lookup?term=$query")

    return when (response) {
        is ApiResult.Success -> ApiResult.Success(response.data)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}