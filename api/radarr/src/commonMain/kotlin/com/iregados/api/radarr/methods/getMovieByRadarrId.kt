package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.Movie

suspend fun RadarrApi.getMovieByRadarrId(id: Long): ApiResult<Movie> {
    return apiGet<
            Movie
            >("/api/v3/movie/$id")
}
