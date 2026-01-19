package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.SystemStatus

suspend fun RadarrApi.systemStatus(): ApiResult<SystemStatus> {
    return apiGet<
            SystemStatus
            >("/api/v3/system/status")
}
