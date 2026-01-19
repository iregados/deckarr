package com.iregados.deckarr.core.util.extension

fun Int?.toEtaString(): String {
    val eta = this ?: return "Unknown"
    if (eta < 0) return "∞"
    val months = eta / (30 * 24 * 3600)
    val days = (eta % (30 * 24 * 3600)) / (24 * 3600)
    val hours = (eta % (24 * 3600)) / 3600
    val minutes = (eta % 3600) / 60
    val seconds = eta % 60

    return buildString {
        if (months > 0) append("$months mo ")
        if (days > 0) append("$days d ")
        if (hours > 0) append("$hours h ")
        if (minutes > 0) append("$minutes m ")
        if (seconds > 0 || isEmpty()) append("$seconds s")
    }.trim()
}