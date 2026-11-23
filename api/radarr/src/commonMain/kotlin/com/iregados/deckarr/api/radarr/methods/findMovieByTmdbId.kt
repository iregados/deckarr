package com.iregados.deckarr.api.radarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.radarr.dto.Movie

suspend fun RadarrApi.findMovieByTmdbId(id: Long): ApiResult<Movie> {
    return apiGet<
            Movie
            >("/api/v3/movie/lookup/tmdb?tmdbId=$id")
}
