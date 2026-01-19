package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.Movie

suspend fun RadarrApi.getImportListMovies(
    includeRecommendations: Boolean,
    includeTrending: Boolean,
    includePopular: Boolean
): ApiResult<List<Movie>> {
    val response = apiGet<
            List<Movie>
            >("api/v3/importlist/movie?includeRecommendations=$includeRecommendations&includeTrending=$includeTrending&includePopular=$includePopular")

    return when (response) {
        is ApiResult.Success -> ApiResult.Success(response.data)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}
