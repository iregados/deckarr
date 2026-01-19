package com.iregados.deckarr

import android.app.Application
import com.iregados.deckarr.core.di.apiModule
import com.iregados.deckarr.core.di.repositoryModule
import com.iregados.deckarr.feature.downloads.di.downloadsModule
import com.iregados.deckarr.feature.movies.di.moviesModule
import com.iregados.deckarr.feature.navigation.di.navigationModule
import com.iregados.deckarr.feature.search.di.searchModule
import com.iregados.deckarr.feature.series.di.seriesModule
import com.iregados.deckarr.feature.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MainApplication)
            // Load modules
            modules(
                apiModule,
                repositoryModule,
                downloadsModule,
                moviesModule,
                navigationModule,
                searchModule,
                seriesModule,
                settingsModule
            )
        }
    }
}