package com.iregados.deckarr.api.radarr.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.radarr.RadarrApi

suspend fun RadarrApi.removeMovieById(
    movieId: Long,
    deleteFiles: Boolean,
    addImportExclusion: Boolean
): ApiResult<Boolean> {
    return apiDelete("/api/v3/movie/$movieId?deleteFiles=$deleteFiles&addImportExclusion=$addImportExclusion")
}
