package com.iregados.deckarr.api.radarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.radarr.dto.RadarrRootFolder

suspend fun RadarrApi.getRootFolders(): ApiResult<List<RadarrRootFolder>> {
    return apiGet<
            List<RadarrRootFolder>
            >("/api/v3/rootfolder")
}
