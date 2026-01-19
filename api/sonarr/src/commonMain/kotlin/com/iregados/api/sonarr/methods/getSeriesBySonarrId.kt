package com.iregados.api.sonarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.sonarr.dto.Series

suspend fun SonarrApi.getSeriesBySonarrId(id: Long): ApiResult<Series> {
    return apiGet<
            Series
            >("/api/v3/series/$id")
}
