package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.getQualityProfiles(): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.SonarrQualityProfile>> {
    return apiGet<
            List<com.iregados.deckarr.api.sonarr.dto.SonarrQualityProfile>
            >("/api/v3/qualityprofile")
}
