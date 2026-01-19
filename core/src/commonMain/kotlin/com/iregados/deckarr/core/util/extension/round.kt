package com.iregados.deckarr.core.util.extension

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.round(decimals: Int): Float {
    try {
        val factor = 10.0f.pow(decimals)
        return (this * factor).roundToInt() / factor
    } catch (_: Exception) {
        return Float.NaN
    }
}