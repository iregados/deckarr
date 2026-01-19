package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.RadarrRelease

suspend fun RadarrApi.getReleasesByRadarrId(id: Long): ApiResult<List<RadarrRelease>> {
    return apiGet<
            List<RadarrRelease>
            >("/api/v3/release?movieId=$id")
}
