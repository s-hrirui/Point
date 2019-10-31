package com.cs.baselib.ext

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi

val Context.isRTLLayout: Boolean
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    get() = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun View.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun View.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}