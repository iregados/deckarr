package com.iregados.deckarr.core.util.dto

enum class ReleaseSortOption(
    val label: String,
    val defaultAscending: Boolean,
) {
    APPROVED("Approved", defaultAscending = false),
    SEEDERS("Seeders", defaultAscending = false),
    SIZE("Size", defaultAscending = true),
    QUALITY("Quality", defaultAscending = false),
    INDEXER("Indexer", defaultAscending = true),
    AGE("Age", defaultAscending = true),
}