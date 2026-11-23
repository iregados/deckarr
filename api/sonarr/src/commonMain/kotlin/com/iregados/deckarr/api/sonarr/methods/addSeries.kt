package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.addSeries(
    series: com.iregados.deckarr.api.sonarr.dto.Series
): ApiResult<com.iregados.deckarr.api.sonarr.dto.Series> {
    return apiPost<
            com.iregados.deckarr.api.sonarr.dto.Series, com.iregados.deckarr.api.sonarr.dto.Series
            >("/api/v3/series", series)
}
