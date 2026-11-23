package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.getReleases(
    seriesId: Long,
    seasonNumber: Long
): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.SonarrRelease>> {
    return apiGet<
            List<com.iregados.deckarr.api.sonarr.dto.SonarrRelease>
            >("/api/v3/release?seriesId=${seriesId}&seasonNumber=${seasonNumber}")
}

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.getReleases(
    episodeId: Long,
): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.SonarrRelease>> {
    return apiGet<
            List<com.iregados.deckarr.api.sonarr.dto.SonarrRelease>
            >("/api/v3/release?episodeId=${episodeId}")
}

