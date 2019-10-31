package com.cs.baselib.ext

import android.util.Log
import com.cs.baselib.BuildConfig


const val TAG = "point"

var showLog = BuildConfig.DEBUG

private enum class LEVEL {
    V, D, I, W, E
}

fun String.v(tag: String = TAG) = log(LEVEL.V, tag, this)
fun String.d(tag: String = TAG) = log(LEVEL.D, tag, this)
fun String.i(tag: String = TAG) = log(LEVEL.I, tag, this)
fun String.w(tag: String = TAG) = log(LEVEL.W, tag, this)
fun String.e(tag: String = TAG) = log(LEVEL.E, tag, this)

private fun log(level: LEVEL, tag: String, message: String) {
    if (!showLog) return
    when (level) {
        LEVEL.V -> Log.v(tag, message)
        LEVEL.D -> Log.d(tag, message)
        LEVEL.I -> Log.i(tag, message)
        LEVEL.W -> Log.w(tag, message)
        LEVEL.E -> Log.e(tag, message)
    }
}