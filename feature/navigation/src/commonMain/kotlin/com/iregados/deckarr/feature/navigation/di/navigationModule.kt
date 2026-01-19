package com.iregados.deckarr.feature.navigation.di

import com.iregados.deckarr.feature.navigation.NavigationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val navigationModule = module {
    viewModelOf(::NavigationViewModel)
}