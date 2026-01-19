package com.iregados.deckarr.feature.downloads.di

import com.iregados.deckarr.feature.downloads.DownloadsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val downloadsModule = module {
    viewModelOf(::DownloadsViewModel)
}