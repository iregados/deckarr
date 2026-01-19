package com.iregados.api.sonarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.sonarr.dto.Episode

suspend fun SonarrApi.getEpisodesBySonarrId(
    seriesId: Long
): ApiResult<List<Episode>> {
    return apiGet<
            List<Episode>
            >("/api/v3/episode?seriesId=${seriesId}")
}
