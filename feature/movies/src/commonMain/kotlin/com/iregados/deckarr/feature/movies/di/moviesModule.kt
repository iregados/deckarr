package com.iregados.deckarr.feature.movies.di

import com.iregados.deckarr.feature.movies.movie_detail.MovieDetailViewModel
import com.iregados.deckarr.feature.movies.movies.MoviesViewModel
import com.iregados.deckarr.feature.movies.search_movie_release.SearchMovieReleaseViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val moviesModule = module {
    viewModelOf(::MoviesViewModel)
    viewModelOf(::MovieDetailViewModel)
    viewModelOf(::SearchMovieReleaseViewModel)
}