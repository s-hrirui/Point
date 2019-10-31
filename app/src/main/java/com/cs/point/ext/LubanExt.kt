package com.cs.point.ext

import android.content.ComponentCallbacks
import android.content.Context
import android.net.Uri
import top.zibin.luban.Luban
import java.io.File
import android.os.Environment.getExternalStorageDirectory
import android.text.TextUtils
import top.zibin.luban.CompressionPredicate
import top.zibin.luban.OnCompressListener


fun compress(context: Context, path: String,callback: (File?)->Unit) {
    Luban.with(context)
        .load(path)
        .ignoreBy(100)
        .setTargetDir(path())
        .setFocusAlpha(false)
        .filter(CompressionPredicate() {
            return@CompressionPredicate !(TextUtils.isEmpty(it) || it.toLowerCase().endsWith(".gif"))
        }
        )
        .setCompressListener(object :OnCompressListener{
            override fun onSuccess(file: File?) {
                callback.invoke(file)
            }

            override fun onError(e: Throwable?) {

            }

            override fun onStart() {

            }

        }).launch();
}

fun compress(context: Context, path: Uri,callback: (File?)->Unit) {
    Luban.with(context)
        .load(path)
        .ignoreBy(100)
        .setTargetDir(path())
        .setFocusAlpha(false)
        .filter(CompressionPredicate() {
            return@CompressionPredicate !(TextUtils.isEmpty(it) || it.toLowerCase().endsWith(".gif"))
        }
        )
        .setCompressListener(object :OnCompressListener{
            override fun onSuccess(file: File?) {
                callback.invoke(file)
            }

            override fun onError(e: Throwable?) {

            }

            override fun onStart() {

            }

        }).launch();
}

fun compress(context: Context, path: File,callback: (File?)->Unit) {
    Luban.with(context)
        .load(path)
        .ignoreBy(100)
        .setTargetDir(path())
        .setFocusAlpha(false)
        .filter(CompressionPredicate() {
            return@CompressionPredicate !(TextUtils.isEmpty(it) || it.toLowerCase().endsWith(".gif"))
            }
        )
        .setCompressListener(object :OnCompressListener{
            override fun onSuccess(file: File?) {
                callback.invoke(file)
            }

            override fun onError(e: Throwable?) {

            }

            override fun onStart() {

            }

        }).launch();
}

private fun path(): String {
    val path = "${getExternalStorageDirectory()}/Luban/image/"
    val file = File(path)
    return if (file.mkdirs()) {
        path
    } else path
}