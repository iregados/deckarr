package com.iregados.deckarr.core.util.extension

import kotlin.math.roundToInt

fun Int?.toFormatedSpeed(): String {
    val bytesPerSec = this ?: return "0 KB/s"
    val kb = bytesPerSec / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0

    return when {
        gb >= 1.0 -> "${(gb * 100).roundToInt() / 100.0} GB/s"
        mb >= 1.0 -> "${(mb * 100).roundToInt() / 100.0} MB/s"
        else -> "${(kb * 10).roundToInt() / 10.0} KB/s"
    }
}