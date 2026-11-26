package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.sonarr.SonarrApi
import com.iregados.deckarr.api.sonarr.dto.SonarrRelease

suspend fun SonarrApi.addRelease(
    sonarrRelease: SonarrRelease,
): ApiResult<SonarrRelease> {
    return apiPost<
            SonarrRelease, SonarrRelease
            >("/api/v3/release", sonarrRelease)
}
