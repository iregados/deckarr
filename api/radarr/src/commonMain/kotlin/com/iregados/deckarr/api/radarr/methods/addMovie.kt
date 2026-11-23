package com.iregados.deckarr.api.radarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.radarr.dto.Movie

suspend fun RadarrApi.addMovie(
    movie: Movie
): ApiResult<Movie> {
    return apiPost<
            Movie, Movie
            >("/api/v3/movie", movie)
}
