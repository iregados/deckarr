package com.iregados.api.sonarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.sonarr.dto.SystemStatus

suspend fun SonarrApi.systemStatus(): ApiResult<SystemStatus> {
    return apiGet<
            SystemStatus
            >("/api/v3/system/status")
}
