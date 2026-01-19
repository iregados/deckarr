package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.radarr.dto.RadarrRootFolder

suspend fun RadarrApi.getRootFolders(): ApiResult<List<RadarrRootFolder>> {
    return apiGet<
            List<RadarrRootFolder>
            >("/api/v3/rootfolder")
}
