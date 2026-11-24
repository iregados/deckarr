package com.iregados.deckarr.core.util.extension

import com.iregados.deckarr.api.common.dto.CoverType
import com.iregados.deckarr.api.sonarr.dto.OriginalLanguage
import com.iregados.deckarr.api.sonarr.dto.Ratings
import com.iregados.deckarr.api.sonarr.dto.Series
import com.iregados.deckarr.api.sonarr.dto.SeriesStatistics
import com.iregados.deckarr.api.sonarr.dto.SonarrImage
import com.iregados.deckarr.api.tmdb.dto.TmdbSeries
import kotlin.math.roundToInt

fun TmdbSeries.toSonarrSeries(): Series = Series(
    id = null,
    title = name,
    sortTitle = name,
    status = "",
    ended = false,
    overview = overview,
    previousAiring = null,
    network = null,
    airTime = null,
    images = listOf(
        SonarrImage(
            coverType = CoverType.Poster,
            url = null,
            remoteUrl = posterUrl,
        )
    ),
    originalLanguage = OriginalLanguage(id = 0L, name = ""),
    seasons = emptyList(),
    year = firstAirDate?.take(4)?.toLongOrNull() ?: 0L,
    path = null,
    qualityProfileId = 0L,
    seasonFolder = false,
    monitored = false,
    monitorNewItems = "",
    useSceneNumbering = false,
    runtime = 0L,
    tvdbId = 0L,
    tvRageId = 0L,
    tvMazeId = 0L,
    tmdbId = id,
    imdbId = null,
    firstAired = firstAirDate,
    lastAired = null,
    seriesType = "",
    cleanTitle = name,
    titleSlug = "",
    rootFolderPath = null,
    certification = null,
    genres = emptyList(),
    added = "",
    ratings = Ratings(
        votes = voteCount,
        value = (voteAverage * 10).roundToInt() / 10.0,
    ),
    statistics = SeriesStatistics(
        seasonCount = 0L,
        episodeFileCount = 0L,
        episodeCount = 0L,
        totalEpisodeCount = 0L,
        sizeOnDisk = 0L,
        releaseGroups = null,
        percentOfEpisodes = 0.0,
    ),
    languageProfileId = 0L,
    nextAiring = null,
    addOptions = null,
)