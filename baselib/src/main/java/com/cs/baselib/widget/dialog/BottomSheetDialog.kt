package com.dizoo.jixx.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.cs.baselib.R
import java.util.ArrayList

class BottomSheetDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private var txt_title: TextView? = null
    private var txt_cancel: TextView? = null
    private var lLayout_content: LinearLayout? = null
    private var sLayout_content: ScrollView? = null
    private var view_line: View? = null
    private var showTitle = false
    private var sheetItemList: MutableList<SheetItem>? = null
    private val display: Display

    init {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
    }

    fun builder(): BottomSheetDialog {
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(R.layout.layout_actionsheet, null)

        // 设置Dialog最小宽度为屏幕宽度
        view.minimumWidth = display.width

        // 获取自定义Dialog布局中的控件
        sLayout_content = view.findViewById<View>(R.id.sLayout_content) as ScrollView
        view_line = view.findViewById<View>(R.id.view_line) as View
        lLayout_content = view.findViewById<View>(R.id.lLayout_content) as LinearLayout
        txt_title = view.findViewById<View>(R.id.txt_title) as TextView
        txt_cancel = view.findViewById<View>(R.id.txt_cancel) as TextView
        txt_cancel!!.setOnClickListener { dialog!!.dismiss() }

        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.ActionSheetDialogStyle)
        dialog!!.setContentView(view)
        val dialogWindow = dialog!!.window
        dialogWindow!!.setGravity(Gravity.LEFT or Gravity.BOTTOM)
        val lp = dialogWindow.attributes
        lp.x = 0
        lp.y = 0
        dialogWindow.attributes = lp
        return this
    }

    fun setTitle(title: String): BottomSheetDialog {
        showTitle = true
        txt_title!!.visibility = View.VISIBLE
        txt_title!!.text = title
        view_line!!.visibility = View.VISIBLE
        return this
    }

    fun setCancelable(cancel: Boolean): BottomSheetDialog {
        dialog!!.setCancelable(cancel)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): BottomSheetDialog {
        dialog!!.setCanceledOnTouchOutside(cancel)
        return this
    }

    /**
     * @param strItem  条目名称
     * @param color    条目字体颜色，设置null则默认蓝色
     * @param listener
     * @return
     */
    fun addSheetItem(strItem: String, @ColorRes color: Int,
                     listener: OnSheetItemClickListener): BottomSheetDialog {
        if (sheetItemList == null) {
            sheetItemList = ArrayList()
        }
        sheetItemList!!.add(SheetItem(strItem, color, listener))
        return this
    }

    fun show() {
        setSheetItems()
        dialog!!.show()
    }

    /**
     * 设置条目布局
     */
    private fun setSheetItems() {
        if (sheetItemList == null || sheetItemList!!.size <= 0) {
            return
        }

        val size = sheetItemList!!.size

        //  高度控制，非最佳解决办法,添加条目过多的时候控制高度
        if (size >= 7) {
            val params = sLayout_content!!.layoutParams as LinearLayout.LayoutParams
            params.height = display.height / 2
            sLayout_content!!.layoutParams = params
        }

        // 循环添加条目
        for (i in 1..size) {
            val sheetItem = sheetItemList!![i - 1]
            val strItem = sheetItem.name
            val color = sheetItem.color
            val listener = sheetItem.itemClickListener

            val textView = TextView(context)
            textView.text = strItem
            textView.textSize = 18f
            textView.gravity = Gravity.CENTER
            if (size == 1){
                if (showTitle){
                    textView.setBackgroundResource(R.drawable.shape_bottom_sheet_dialog_text_bottom_bg)
                }else{
                    textView.setBackgroundResource(R.drawable.shape_bottom_sheet_dialog_bg)
                }
            }else{
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundColor(ContextCompat.getColor(context,android.R.color.white))
                    } else {
                        textView.setBackgroundResource(R.drawable.shape_bottom_sheet_dialog_text_bottom_bg)
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.shape_bottom_sheet_dialog_text_top_bg)
                    } else if (i < size) {
                        textView.setBackgroundColor(ContextCompat.getColor(context,android.R.color.white))
                    } else {
                        textView.setBackgroundResource(R.drawable.shape_bottom_sheet_dialog_text_bottom_bg)
                    }
                }
            }
            textView.setTextColor(ContextCompat.getColor(context, color))
            // 高度
            val scale = context.resources.displayMetrics.density
            val height = (45 * scale + 0.5f).toInt()
            textView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, height)

            // 点击事件
            textView.setOnClickListener {
                listener.onClick(i)
                dialog!!.dismiss()
            }

            lLayout_content!!.addView(textView)
        }
    }

    fun dismiss() {
        if (dialog != null || dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    interface OnSheetItemClickListener {
        fun onClick(which: Int)
    }

    inner class SheetItem(internal var name: String, @param:ColorRes @field:ColorRes internal var color: Int,
                          internal var itemClickListener: OnSheetItemClickListener)
}
