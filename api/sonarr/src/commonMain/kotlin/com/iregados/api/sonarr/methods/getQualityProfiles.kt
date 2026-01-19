package com.iregados.api.sonarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.sonarr.dto.SonarrQualityProfile

suspend fun SonarrApi.getQualityProfiles(): ApiResult<List<SonarrQualityProfile>> {
    return apiGet<
            List<SonarrQualityProfile>
            >("/api/v3/qualityprofile")
}
