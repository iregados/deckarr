package com.iregados.deckarr.api.radarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.radarr.dto.RadarrRelease

suspend fun RadarrApi.addRelease(
    radarrRelease: RadarrRelease
): ApiResult<RadarrRelease> {
    return apiPost<
            RadarrRelease, RadarrRelease
            >("/api/v3/release", radarrRelease)
}
