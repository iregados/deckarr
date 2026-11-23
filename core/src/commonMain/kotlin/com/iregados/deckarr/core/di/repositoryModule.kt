package com.iregados.deckarr.core.di

import com.iregados.deckarr.core.data.dao.DataStoreDao
import com.iregados.deckarr.core.data.repository.DownloadsRepositoryImpl
import com.iregados.deckarr.core.data.repository.PreferencesRepositoryImpl
import com.iregados.deckarr.core.data.repository.RadarrRepositoryImpl
import com.iregados.deckarr.core.data.repository.SonarrRepositoryImpl
import com.iregados.deckarr.core.data.repository.TorrentClientFactoryImpl
import com.iregados.deckarr.core.domain.DownloadsRepository
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.domain.SonarrRepository
import com.iregados.deckarr.core.domain.TorrentClientFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    //LOCAL
    single { DataStoreDao() }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    //REMOTE
    single<TorrentClientFactory> { TorrentClientFactoryImpl() }
    single<RadarrRepository> { RadarrRepositoryImpl(get(), get(named("AppScope"))) }
    single<SonarrRepository> { SonarrRepositoryImpl(get(), get(named("AppScope"))) }
    single<DownloadsRepository> { DownloadsRepositoryImpl(get(), get(), get(named("AppScope"))) }
}