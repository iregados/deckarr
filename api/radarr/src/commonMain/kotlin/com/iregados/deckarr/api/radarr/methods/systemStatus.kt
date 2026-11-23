package com.iregados.deckarr.api.radarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.radarr.dto.SystemStatus

suspend fun RadarrApi.systemStatus(): ApiResult<SystemStatus> {
    return apiGet<
            SystemStatus
            >("/api/v3/system/status")
}
