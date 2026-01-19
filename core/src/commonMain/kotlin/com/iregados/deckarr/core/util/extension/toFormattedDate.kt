package com.iregados.deckarr.core.util.extension

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun String?.toFormattedDate(): String {
    return this?.let {
        val date = Instant.parse(this)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val day = date.day.toString().padStart(2, '0')
        val month = date.month.number.toString().padStart(2, '0')
        val year = date.year.toString().padStart(4, '0')
        "$day/$month/$year"
    } ?: "Unknown"

}