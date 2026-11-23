package com.iregados.deckarr.feature.movies.movie_detail

sealed class MovieDetailEvent {
    data class AddMovie(
        val qualityProfileId: Long,
        val rootFolderPath: String,
        val shouldMonitor: Boolean,
    ) : MovieDetailEvent()

    data class RemoveMovie(
        val addToExclusionList: Boolean,
        val deleteFiles: Boolean,
    ) : MovieDetailEvent()
}