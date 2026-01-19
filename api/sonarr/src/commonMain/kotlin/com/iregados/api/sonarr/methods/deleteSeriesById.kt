package com.iregados.api.sonarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi

suspend fun SonarrApi.removeSeriesById(
    seriesId: Long,
    deleteFiles: Boolean,
    addImportExclusion: Boolean
): ApiResult<Boolean> {
    return apiDelete("/api/v3/series/$seriesId?deleteFiles=$deleteFiles&addImportExclusion=$addImportExclusion")
}
