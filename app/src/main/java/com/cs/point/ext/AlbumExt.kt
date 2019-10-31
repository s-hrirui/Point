package com.cs.point.ext

import android.Manifest
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.cs.baselib.ext.toast
import com.cs.point.R
import com.dizoo.jixx.util.DialogUtil
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.SelectionCreator
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import luyao.util.ktx.ext.permission.PermissionRequest
import luyao.util.ktx.ext.permission.requestPermission


const val ALBUM_REQUEST_CODE = 10011

fun FragmentActivity.album(
    capture: Boolean = false,
    maxSelectable: Int = 1, @StyleRes theme: Int = R.style.Matisse_Zhihu
) {
    var selectionCreator = Matisse
        .from(this)
        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.WEBP))
    album(selectionCreator, theme, capture, maxSelectable)
}

fun Fragment.album(
    capture: Boolean = false,
    maxSelectable: Int = 1, @StyleRes theme: Int = R.style.Matisse_Zhihu
) {
    var selectionCreator = Matisse
        .from(this)
        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.WEBP))
    album(selectionCreator, theme, capture, maxSelectable)
}

fun requestAlbumPermission(activity: FragmentActivity, callback:()->Unit){
    activity.requestPermission(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        callbacks = {
            onGranted { callback.invoke() }
            onDenied { toast(activity,"权限被拒绝") }
            onShowRationale { againRequest(activity,it) }
            onNeverAskAgain {
                if (it.isNotEmpty() && it.contains("android.permission.CAMERA") || it.contains("android.permission.READ_EXTERNAL_STORAGE")) {
                    toast(activity,"橙赚需要开启相关权限，您是否允许？在设置-应用-橙赚-权限中开启电话权限，以正常读取设备码")
                } else {
                   TODO("跳转设置开启权限")
                }
            }
        }
    )
}

private fun againRequest(activity: FragmentActivity,it: PermissionRequest) {
    DialogUtil.showCommonDoubleOptionDialog(
        activity,
        "提示",
        "权限被拒绝",
        confirm = {
            it.permissionFragment.requestPermissionsByFragment(
                it.permissions.toTypedArray(),
                it.requestCode
            )
        }, cancel = {})
}

private fun album(
    selectionCreator: SelectionCreator,
    theme: Int,
    capture: Boolean,
    table: Int
) {
    selectionCreator
        .theme(theme)
        .countable(false)
        .capture(capture)
        .captureStrategy(CaptureStrategy(true, "com.cz.point.fileprovider", "point"))
        .maxSelectable(table)
        .originalEnable(true)
        .maxOriginalSize(10)
        .imageEngine(GlideEngine())
        .forResult(ALBUM_REQUEST_CODE);
}


