package com.cs.baselib.widget.dialog

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.TextView

import androidx.appcompat.app.AlertDialog

import com.cs.baselib.R


class LoadingDialog(private val activity: Context) : AlertDialog(activity, R.style.CustomDialog) {


    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        setContentView(R.layout.layout_loading_dialog)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> if (isShowing) {
                dismiss()
            }
        }
        return true
    }

}
