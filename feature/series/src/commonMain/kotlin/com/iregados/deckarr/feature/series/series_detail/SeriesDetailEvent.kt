package com.iregados.deckarr.feature.series.series_detail

sealed class SeriesDetailEvent {
    data class AddSeries(
        val qualityProfileId: Long,
        val rootFolderPath: String,
        val shouldMonitor: Boolean,
    ) : SeriesDetailEvent()

    data class RemoveSeries(
        val addToExclusionList: Boolean,
        val deleteFiles: Boolean,
    ) : SeriesDetailEvent()
}