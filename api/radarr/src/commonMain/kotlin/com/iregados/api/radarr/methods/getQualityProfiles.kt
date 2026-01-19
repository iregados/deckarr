package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.RadarrQualityProfile

suspend fun RadarrApi.getQualityProfiles(): ApiResult<List<RadarrQualityProfile>> {
    return apiGet<
            List<RadarrQualityProfile>
            >("/api/v3/qualityprofile")
}
