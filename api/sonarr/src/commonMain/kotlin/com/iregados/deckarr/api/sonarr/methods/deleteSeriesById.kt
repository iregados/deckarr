package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.removeSeriesById(
    seriesId: Long,
    deleteFiles: Boolean,
    addImportExclusion: Boolean
): ApiResult<Boolean> {
    return apiDelete("/api/v3/series/$seriesId?deleteFiles=$deleteFiles&addImportExclusion=$addImportExclusion")
}
