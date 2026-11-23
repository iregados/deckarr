package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.getSeriesByTvdbId(tvdbId: Long): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.Series>> {
    val response = apiGet<
            List<com.iregados.deckarr.api.sonarr.dto.Series>
            >("/api/v3/series/lookup?term=tvdbId:$tvdbId")

    return when (response) {
        is ApiResult.Success -> ApiResult.Success(
            response.data.firstOrNull()?.let { listOf(it) } ?: emptyList()
        )

        is ApiResult.Error -> ApiResult.Error(response.message)
    }

}