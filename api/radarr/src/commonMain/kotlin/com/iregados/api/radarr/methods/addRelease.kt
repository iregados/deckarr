package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.RadarrRelease

suspend fun RadarrApi.addRelease(
    radarrRelease: RadarrRelease
): ApiResult<RadarrRelease> {
    return apiPost<
            RadarrRelease, RadarrRelease
            >("/api/v3/release", radarrRelease)
}
