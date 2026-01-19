package com.iregados.deckarr.core.domain.dto

import com.iregados.api.radarr.dto.Movie

data class ImportListMoviesResponse(
    val recommendations: List<Movie>?,
    val trending: List<Movie>?,
    val popular: List<Movie>?
)
