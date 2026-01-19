package com.iregados.deckarr.feature.search

sealed interface SearchType {
    object Movies : SearchType
    object Series : SearchType
}
