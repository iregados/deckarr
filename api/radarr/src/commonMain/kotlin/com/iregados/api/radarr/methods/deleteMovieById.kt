package com.iregados.api.radarr.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.radarr.RadarrApi

suspend fun RadarrApi.removeMovieById(
    movieId: Long,
    deleteFiles: Boolean,
    addImportExclusion: Boolean
): ApiResult<Boolean> {
    return apiDelete("/api/v3/movie/$movieId?deleteFiles=$deleteFiles&addImportExclusion=$addImportExclusion")
}
