package com.dizoo.jixx.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.cs.baselib.R


class CommonAlertDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private var container: LinearLayout? = null
    private var titleTv: TextView? = null
    private var msgTv: TextView? = null
    private var negBtn: Button? = null
    private var posBtn: Button? = null
    private var line: View? = null
    private val display: Display
    private var showTitle = false
    private var showMsg = false
    private var showPosBtn = false
    private var showNegBtn = false

    init {
        val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
    }

    fun init(): CommonAlertDialog {
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(R.layout.layout_alertdialog, null)

        // 获取自定义Dialog布局中的控件
        container = view.findViewById<View>(R.id.container) as LinearLayout
        titleTv = view.findViewById<View>(R.id.txt_title) as TextView
        titleTv!!.visibility = View.GONE
        msgTv = view.findViewById<View>(R.id.txt_msg) as TextView
        msgTv!!.visibility = View.GONE
        negBtn = view.findViewById<View>(R.id.btn_neg) as Button
        negBtn!!.visibility = View.GONE
        posBtn = view.findViewById<View>(R.id.btn_pos) as Button
        posBtn!!.visibility = View.GONE
        line = view.findViewById<View>(R.id.img_line)
        line!!.visibility = View.GONE

        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.AlertDialogStyle)
        dialog!!.setContentView(view)

        // 调整dialog背景大小
        container!!.layoutParams = FrameLayout.LayoutParams(
                (display.width * 0.85).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT)

        return this
    }

    fun setTitle(title: String): CommonAlertDialog {
        showTitle = true
        if ("" == title) {
            titleTv!!.text = "提示"
        } else {
            titleTv!!.text = title
        }
        return this
    }

    fun setMsg(msg: String): CommonAlertDialog {
        showMsg = true
        if ("" == msg) {
            msgTv!!.text = "内容"
        } else {
            msgTv!!.text = msg
        }
        return this
    }

    fun setCancelable(cancel: Boolean): CommonAlertDialog {
        dialog!!.setCancelable(cancel)
        return this
    }

    fun setPositiveButton(text: String,
                          listener: View.OnClickListener): CommonAlertDialog {
        showPosBtn = true
        if ("" == text) {
            posBtn!!.text = "确定"
        } else {
            posBtn!!.text = text
        }
        posBtn!!.setOnClickListener { v ->
            listener.onClick(v)
            dialog!!.dismiss()
        }
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): CommonAlertDialog {
        dialog?.setCanceledOnTouchOutside(cancel)
        return this
    }

    fun setNegativeButton(text: String, listener: View.OnClickListener): CommonAlertDialog {
        showNegBtn = true
        if ("" == text) {
            negBtn!!.text = "取消"
        } else {
            negBtn!!.text = text
        }
        negBtn!!.setOnClickListener { v ->
            listener.onClick(v)
            dialog!!.dismiss()
        }
        return this
    }

    private fun setLayout() {
        if (!showTitle && !showMsg) {
            titleTv!!.text = "提示"
            titleTv!!.visibility = View.VISIBLE
        }

        if (showTitle) {
            titleTv!!.visibility = View.VISIBLE
        }

        if (showMsg) {
            msgTv!!.visibility = View.VISIBLE
        }

        if (!showPosBtn && !showNegBtn) {
            posBtn!!.text = "确定"
            posBtn!!.visibility = View.VISIBLE
            posBtn!!.setOnClickListener { dialog!!.dismiss() }
        }

        if (showPosBtn && showNegBtn) {
            posBtn!!.visibility = View.VISIBLE
            negBtn!!.visibility = View.VISIBLE
            line!!.visibility = View.VISIBLE
        }

        if (showPosBtn && !showNegBtn) {
            posBtn!!.visibility = View.VISIBLE
        }

        if (!showPosBtn && showNegBtn) {
            negBtn!!.visibility = View.VISIBLE
        }
    }

    fun show() {
        setLayout()
        dialog!!.show()
    }
}