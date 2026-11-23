package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.systemStatus(): ApiResult<com.iregados.deckarr.api.sonarr.dto.SystemStatus> {
    return apiGet<
            com.iregados.deckarr.api.sonarr.dto.SystemStatus
            >("/api/v3/system/status")
}
