package com.iregados.api.sonarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.sonarr.dto.Series

suspend fun SonarrApi.getSeries(): ApiResult<List<Series>> {
    return apiGet<
            List<Series>
            >("/api/v3/series")
}
