package com.iregados.deckarr.api.radarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.radarr.dto.RadarrQualityProfile

suspend fun RadarrApi.getQualityProfiles(): ApiResult<List<RadarrQualityProfile>> {
    return apiGet<
            List<RadarrQualityProfile>
            >("/api/v3/qualityprofile")
}
