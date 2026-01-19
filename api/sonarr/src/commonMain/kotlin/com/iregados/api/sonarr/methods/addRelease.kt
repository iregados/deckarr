package com.iregados.api.sonarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.sonarr.dto.SonarrRelease

suspend fun SonarrApi.addRelease(
    sonarrRelease: SonarrRelease,
): ApiResult<SonarrRelease> {
    return apiPost<
            SonarrRelease, SonarrRelease
            >("/api/v3/release", sonarrRelease)
}
