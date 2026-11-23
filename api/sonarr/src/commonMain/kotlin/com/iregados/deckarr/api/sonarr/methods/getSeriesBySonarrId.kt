package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.getSeriesBySonarrId(id: Long): ApiResult<com.iregados.deckarr.api.sonarr.dto.Series> {
    return apiGet<
            com.iregados.deckarr.api.sonarr.dto.Series
            >("/api/v3/series/$id")
}
