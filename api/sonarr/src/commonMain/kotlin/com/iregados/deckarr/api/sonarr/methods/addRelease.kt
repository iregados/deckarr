package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.addRelease(
    sonarrRelease: com.iregados.deckarr.api.sonarr.dto.SonarrRelease,
): ApiResult<com.iregados.deckarr.api.sonarr.dto.SonarrRelease> {
    return apiPost<
            com.iregados.deckarr.api.sonarr.dto.SonarrRelease, com.iregados.deckarr.api.sonarr.dto.SonarrRelease
            >("/api/v3/release", sonarrRelease)
}
