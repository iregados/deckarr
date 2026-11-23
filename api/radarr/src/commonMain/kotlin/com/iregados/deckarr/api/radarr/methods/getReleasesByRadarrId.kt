package com.iregados.deckarr.api.radarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.radarr.dto.RadarrRelease

suspend fun RadarrApi.getReleasesByRadarrId(id: Long): ApiResult<List<RadarrRelease>> {
    return apiGet<
            List<RadarrRelease>
            >("/api/v3/release?movieId=$id")
}
