package com.iregados.deckarr.core.util.extension

fun Long?.toFormatedSize(): String {
    if (this == null) return "Unknown"
    if (this == 0L) return "None"
    val bytes = this
    val kb = bytes / 1024f
    val mb = kb / 1024f
    val gb = mb / 1024f
    val tb = gb / 1024f

    return when {
        tb >= 1.0 -> "${tb.round(2)} TB"
        gb >= 1.0 -> "${gb.round(if (gb > 100) 1 else 2)} GB"
        mb >= 1.0 -> "${mb.round(2)} MB"
        else -> "${kb.round(2)} KB"
    }
}