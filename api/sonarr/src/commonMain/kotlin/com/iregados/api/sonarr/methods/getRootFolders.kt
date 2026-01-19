package com.iregados.api.sonarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.sonarr.dto.SonarrRootFolder

suspend fun SonarrApi.getRootFolders(): ApiResult<List<SonarrRootFolder>> {
    return apiGet<
            List<SonarrRootFolder>
            >("/api/v3/rootfolder")
}
