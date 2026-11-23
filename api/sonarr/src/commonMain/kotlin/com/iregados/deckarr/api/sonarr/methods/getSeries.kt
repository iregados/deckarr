package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.getSeries(): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.Series>> {
    return apiGet<
            List<com.iregados.deckarr.api.sonarr.dto.Series>
            >("/api/v3/series")
}
