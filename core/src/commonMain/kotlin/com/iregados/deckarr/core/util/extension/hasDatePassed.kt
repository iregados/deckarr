package com.iregados.deckarr.core.util.extension

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun String?.hasDatePassed(): Boolean {
    return this?.let {
        val instantString = when {
            it.contains("T") && it.contains("Z") -> it
            it.contains("T") -> "${it}Z"
            else -> "${it}T00:00:00Z"
        }
        val date = Instant.parse(instantString)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return date < today
    } ?: false

}