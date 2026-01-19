package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.Movie

suspend fun RadarrApi.addMovie(
    movie: Movie
): ApiResult<Movie> {
    return apiPost<
            Movie, Movie
            >("/api/v3/movie", movie)
}
