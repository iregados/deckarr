package com.iregados.api.sonarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.sonarr.dto.SonarrRelease

suspend fun SonarrApi.getReleases(
    seriesId: Long,
    seasonNumber: Long
): ApiResult<List<SonarrRelease>> {
    return apiGet<
            List<SonarrRelease>
            >("/api/v3/release?seriesId=${seriesId}&seasonNumber=${seasonNumber}")
}

suspend fun SonarrApi.getReleases(
    episodeId: Long,
): ApiResult<List<SonarrRelease>> {
    return apiGet<
            List<SonarrRelease>
            >("/api/v3/release?episodeId=${episodeId}")
}

