package com.cs.baselib.ext

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.cs.baselib.app.BaseApplication


fun Context.toast(content: String) {
    showToast(this, content)
}

fun Context.toast(@StringRes id: Int) {
    showToast(this, this.getString(id))
}

fun Activity.toast(content: String) {
    showToast(this, content)
}

fun Activity.toast(@StringRes id: Int) {
    showToast(this, getString(id))
}

fun Fragment.toast(content: String) {
    showToast(BaseApplication.instance, content)
}

fun Fragment.toast(@StringRes id: Int) {
    var context = BaseApplication.instance
    showToast(context, context.getString(id))
}

fun Any.toast(context: Context, content: String) {
    showToast(context, content)
}

fun Any.toast(context: Context, @StringRes id: Int) {
    showToast(context, context.getString(id))
}

private fun showToast(context: Context, content: String) {
    Toast.makeText(context, content, Toast.LENGTH_SHORT).apply {
        show()
    }
}



