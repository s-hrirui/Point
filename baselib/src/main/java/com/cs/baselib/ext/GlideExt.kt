package com.cs.baselib.ext

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.cs.baselib.widget.GlideApp
import java.io.File

fun ImageView.load(path:String){
    GlideApp.with(this.context)
        .load(path)
        .into(this)
}

fun ImageView.load(@DrawableRes res:Int){
    GlideApp.with(this.context)
        .load(res)
        .into(this)
}

fun ImageView.load(file: File){
    GlideApp.with(this.context)
        .load(file)
        .into(this)
}