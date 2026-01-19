package com.iregados.deckarr.feature.search

sealed class SearchEvent() {
    data class SetQuery(
        val query: String,
    ) : SearchEvent()

    data class SetSearchType(
        val searchType: SearchType,
    ) : SearchEvent()
}
