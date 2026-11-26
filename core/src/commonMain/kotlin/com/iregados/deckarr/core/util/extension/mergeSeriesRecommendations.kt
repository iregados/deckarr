package com.iregados.deckarr.core.util.extension

import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.api.tmdb.dto.TmdbSeries

fun List<List<TmdbSeries>>.mergeSeriesRecommendations(
    existingTmdbIds: Set<Long>,
    limit: Int = 20,
): List<Series> = flatten()
    .filter { it.id !in existingTmdbIds }
    .groupBy { it.id }
    .values
    .sortedWith(
        compareByDescending<List<TmdbSeries>> { it.size }
            .thenByDescending { it.first().voteAverage }
    )
    .take(limit)
    .map { it.first().toSonarrSeries() }