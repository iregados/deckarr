package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.getEpisodesBySonarrId(
    seriesId: Long
): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.Episode>> {
    return apiGet<
            List<com.iregados.deckarr.api.sonarr.dto.Episode>
            >("/api/v3/episode?seriesId=${seriesId}")
}
