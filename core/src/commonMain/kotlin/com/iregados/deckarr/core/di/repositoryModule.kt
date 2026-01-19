package com.iregados.deckarr.core.di

import com.iregados.deckarr.core.data.dao.DataStoreDao
import com.iregados.deckarr.core.domain.DownloadsRepository
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.domain.SonarrRepository
import org.koin.dsl.module

val repositoryModule = module {
    //LOCAL
    single { DataStoreDao() }
    single { PreferencesRepository(get()) }

    //REMOTE
    single { RadarrRepository(get()) }
    single { SonarrRepository(get()) }
    single { DownloadsRepository(get()) }
}