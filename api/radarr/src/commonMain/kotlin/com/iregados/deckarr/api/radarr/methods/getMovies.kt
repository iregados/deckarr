package com.iregados.deckarr.api.radarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.radarr.dto.Movie

suspend fun RadarrApi.getMovies(): ApiResult<List<Movie>> {
    val response = apiGet<
            List<Movie>
            >("/api/v3/movie")

    return when (response) {
        is ApiResult.Success -> ApiResult.Success(response.data)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}
