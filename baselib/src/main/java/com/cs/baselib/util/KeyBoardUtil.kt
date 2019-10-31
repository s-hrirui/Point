package com.dizoo.jixx.util

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.lang.reflect.Field


object KeyBoardUtil {

    /**
     * 是否落在 EditText 区域
     */
    fun isHideKeyboard(view: View?, event: MotionEvent): Boolean {
        if (view != null && view is EditText) {
            val location = intArrayOf(0, 0)
            view.getLocationInWindow(location)
            //获取现在拥有焦点的控件view的位置，即EditText
            val left = location[0]
            val top = location[1]
            val bottom = top + view.height
            val right = left + view.width
            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            val isInEt = (event.x > left && event.x < right && event.y > top && event.y < bottom)
            return !isInEt
        }
        return false
    }

    /**
     * 关闭软键盘
     */
    fun hideKeyBoard(context: Context, view: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 打卡软键盘
     */
    fun openKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    /**
     * 解决InputMethodManager引起的内存泄漏
     * 在Activity的onDestroy方法里调用
     */
    fun fixInputMethodManagerLeak(context: Context) {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val arr = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        var field: Field? = null
        var objGet: Any? = null
        for (i in arr.indices) {
            val param = arr[i]
            try {
                field = imm.javaClass.getDeclaredField(param)
                if (field.isAccessible === false) {
                    field.isAccessible = true
                }
                objGet = field.get(imm)
                if (objGet != null && objGet is View) {
                    val view = objGet
                    if (view.context === context) {
                        // 被InputMethodManager持有引用的context是想要目标销毁的
                        field.set(imm, null) // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }

        }

    }

}