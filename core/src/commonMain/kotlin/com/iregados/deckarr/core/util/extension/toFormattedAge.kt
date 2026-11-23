package com.iregados.deckarr.core.util.extension

fun Double.toFormattedAge(): String {
    val totalMinutes = toLong().coerceAtLeast(0L)
    return when {
        totalMinutes < 60L -> "${totalMinutes}m"
        totalMinutes < 24L * 60L -> "${totalMinutes / 60L}h"
        else -> "${totalMinutes / (24L * 60L)}d"
    }
}