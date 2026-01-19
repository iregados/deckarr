package com.iregados.deckarr.feature.series.di

import com.iregados.deckarr.feature.series.search_series_release.SearchSeriesReleaseViewModel
import com.iregados.deckarr.feature.series.series.SeriesViewModel
import com.iregados.deckarr.feature.series.series_detail.SeriesDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val seriesModule = module {
    viewModelOf(::SeriesViewModel)
    viewModelOf(::SeriesDetailViewModel)
    viewModelOf(::SearchSeriesReleaseViewModel)
}