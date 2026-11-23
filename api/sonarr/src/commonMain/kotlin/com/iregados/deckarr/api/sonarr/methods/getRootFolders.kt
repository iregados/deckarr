package com.iregados.deckarr.api.sonarr.methods

import com.iregados.deckarr.api.common.ApiResult

suspend fun com.iregados.deckarr.api.sonarr.SonarrApi.getRootFolders(): ApiResult<List<com.iregados.deckarr.api.sonarr.dto.SonarrRootFolder>> {
    return apiGet<
            List<com.iregados.deckarr.api.sonarr.dto.SonarrRootFolder>
            >("/api/v3/rootfolder")
}
