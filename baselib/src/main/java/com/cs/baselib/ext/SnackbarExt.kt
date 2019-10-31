package com.cs.baselib.ext

import android.app.Activity
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cs.baselib.R
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnackMsg(msg: String) {
    val snackbar = Snackbar.make(this.window.decorView, msg, Snackbar.LENGTH_SHORT)
    val view = snackbar.view
    view.findViewById<TextView>(R.id.snackbar_text).setTextColor(ContextCompat.getColor(this, android.R.color.white))
    snackbar.show()
}

fun Fragment.showSnackMsg(msg: String) {
    this.activity ?: return
    val snackbar = Snackbar.make(this.activity!!.window.decorView, msg, Snackbar.LENGTH_SHORT)
    val view = snackbar.view
    view.findViewById<TextView>(R.id.snackbar_text).setTextColor(ContextCompat.getColor(this.activity!!, android.R.color.white))
    snackbar.show()
}