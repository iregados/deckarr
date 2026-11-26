package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.sonarr.SonarrApi
import com.iregados.deckarr.api.sonarr.dto.Series

suspend fun SonarrApi.getSeriesByTvdbId(tvdbId: Long): ApiResult<List<Series>> {
    val response = apiGet<
            List<Series>
            >("/api/v3/series/lookup?term=tvdb:$tvdbId")

    return when (response) {
        is ApiResult.Success -> ApiResult.Success(
            response.data.firstOrNull()?.let { listOf(it) } ?: emptyList()
        )

        is ApiResult.Error -> ApiResult.Error(response.message)
    }

}