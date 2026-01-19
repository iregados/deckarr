package com.iregados.deckarr.feature.navigation

import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.navigation.keys.BaseKey
import com.iregados.deckarr.feature.navigation.keys.HomeKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel : ViewModel() {
    private val _bottomBackStack = MutableStateFlow<List<NavKey>>(listOf(HomeKey))
    val bottomBackStack = _bottomBackStack.asStateFlow()
    private val _mainBackStack = MutableStateFlow<List<NavKey>>(listOf(BaseKey))
    val mainBackStack = _mainBackStack.asStateFlow()

    fun navigateBottomTo(route: NavKey) {
        _bottomBackStack.update { backStack ->
            backStack.filter { it != route } + route
        }
    }

    fun navigateMainTo(route: NavKey) {
        _mainBackStack.update {
            it + route
        }
    }

    fun popBottomBackStack() {
        _bottomBackStack.update { backStack ->
            backStack.filter { it != HomeKey } + HomeKey
        }
    }

    fun popMainBackStack() {
        _mainBackStack.update { it.dropLast(1) }
    }
}