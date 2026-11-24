package com.iregados.deckarr.api.tmdb.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.tmdb.TmdbApi
import com.iregados.deckarr.api.tmdb.dto.TmdbPagedResponse
import com.iregados.deckarr.api.tmdb.dto.TmdbSeries

suspend fun TmdbApi.getTrendingSeries(
    timeWindow: String = "week",
    page: Int = 1
): ApiResult<TmdbPagedResponse<TmdbSeries>> =
    apiGet("/trending/tv/$timeWindow?page=$page")