package com.iregados.deckarr.feature.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.iregados.deckarr.api.radarr.dto.RadarrImage
import com.iregados.deckarr.feature.downloads.keys.DownloadsKey
import com.iregados.deckarr.feature.home.navigation.keys.HomeKey
import com.iregados.deckarr.feature.movies.keys.MoviesListKey
import com.iregados.deckarr.feature.movies.movie_detail.navigation.keys.MovieDetailKey
import com.iregados.deckarr.feature.movies.movies.navigation.keys.MoviesKey
import com.iregados.deckarr.feature.movies.search_movie_release.navigation.keys.SearchMovieReleaseKey
import com.iregados.deckarr.feature.search.navigation.keys.SearchKey
import com.iregados.deckarr.feature.series.search_series_release.navigation.keys.SearchSeriesReleaseKey
import com.iregados.deckarr.feature.series.series.navigation.keys.SeriesKey
import com.iregados.deckarr.feature.series.series_detail.navigation.keys.SeriesDetailKey
import com.iregados.deckarr.feature.series.series_list.navigation.keys.SeriesListKey
import com.iregados.deckarr.feature.settings.navigation.keys.SettingsKey
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val navSavedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        contextual(RadarrImage::class, RadarrImage.serializer())
        
        polymorphic(NavKey::class) {
            subclass(HomeKey::class, HomeKey.serializer())
            subclass(MoviesKey::class, MoviesKey.serializer())
            subclass(MovieDetailKey::class, MovieDetailKey.serializer())
            subclass(MoviesListKey::class, MoviesListKey.serializer())
            subclass(SearchMovieReleaseKey::class, SearchMovieReleaseKey.serializer())
            subclass(SeriesKey::class, SeriesKey.serializer())
            subclass(SeriesDetailKey::class, SeriesDetailKey.serializer())
            subclass(SeriesListKey::class, SeriesListKey.serializer())
            subclass(SearchSeriesReleaseKey::class, SearchSeriesReleaseKey.serializer())
            subclass(DownloadsKey::class, DownloadsKey.serializer())
            subclass(SearchKey::class, SearchKey.serializer())
            subclass(SettingsKey::class, SettingsKey.serializer())
        }
    }
}