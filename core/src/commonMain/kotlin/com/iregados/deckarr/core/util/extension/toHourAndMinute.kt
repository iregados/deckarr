package com.iregados.deckarr.core.util.extension

fun Long.toHourAndMinute(): String {
    val hours = this / 60
    val minutes = this % 60
    if (hours == 0L) return "${minutes}m"
    if (minutes == 0L) return "${hours}h 00m"
    if (minutes <= 9L) return "${hours}h 0${minutes}m"
    return "${hours}h ${minutes}m"
}