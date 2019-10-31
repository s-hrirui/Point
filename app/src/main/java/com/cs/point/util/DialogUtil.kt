package com.dizoo.jixx.util

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import com.cs.baselib.widget.dialog.LoadingDialog
import com.cs.point.R
import com.dizoo.jixx.widget.dialog.BottomSheetDialog
import com.dizoo.jixx.widget.dialog.CommonAlertDialog


object DialogUtil {

    /**
     * 显示loadingDialog
     */
    fun showLoadingDialog(context: Context, message: String = ""): LoadingDialog {
        return LoadingDialog(context)
    }

    /**
     *  两个选择的dialog
     * @param context Context   上下文
     * @param title String      头部提示文字
     * @param message String    Dialog提示
     * @param btConfirm String   确认按钮文字
     * @param btCancel String   取消按钮文字
     * @param canCelable Boolean   设置对话框返回键关闭
     * @param confirm   确定回调接口
     * @param cancel   取消回调接口
     */
    @JvmStatic
    fun showCommonDoubleOptionDialog(
        context: Context,
        title: String = "",
        message: String,
        btConfirm: String = "确定",
        btCancel: String = "取消",
        canCelable: Boolean = false,
        confirm: () -> Unit,
        cancel: () -> Unit
    ) {
        CommonAlertDialog(context)
            .init()
            .setTitle(title)
            .setMsg(message)
            .setCancelable(canCelable)
            .setCanceledOnTouchOutside(canCelable)
            .setPositiveButton(btConfirm, View.OnClickListener { confirm.invoke() })
            .setNegativeButton(btCancel, View.OnClickListener { cancel.invoke() })
            .show()
    }

    @JvmStatic
    fun showAppUpdateDialog(
        context: Context,
        title: String = "",
        message: String,
        btConfirm: String = "确定",
        btCancel: String = "取消",
        canCelable: Boolean = false,
        isDouble: Boolean = true,
        confirm: () -> Unit,
        cancel: () -> Unit
    ) {
        var dialog = CommonAlertDialog(context)
            .init()
            .setTitle(title)
            .setMsg(message)
            .setCancelable(canCelable)
            .setCanceledOnTouchOutside(canCelable)
            .setPositiveButton(btConfirm, View.OnClickListener { confirm.invoke() })
        if (isDouble) {
            dialog.setNegativeButton(btCancel, View.OnClickListener { cancel.invoke() })
        }
        dialog.show()
    }

    /**
     *  一个选择的dialog
     * @param context Context   上下文
     * @param title String      头部提示文字
     * @param message String    Dialog提示
     * @param btConfirm String   确认按钮文字
     * @param canCelable Boolean   设置对话框返回键关闭
     * @param confirm 确定回调接口
     */
    fun showCommonSingleOptionDialog(
        context: Context,
        message: String,
        btConfirm: String = "确定",
        canCelable: Boolean = false,
        confirm: () -> Unit
    ) {
        CommonAlertDialog(context)
            .init()
            .setMsg(message)
            .setCancelable(canCelable)
            .setCanceledOnTouchOutside(canCelable)
            .setNegativeButton(btConfirm, View.OnClickListener { confirm.invoke() })
            .show()
    }

    /**
     *  底部弹出的dialog
     * @param context Context   上下文
     * @param message String    Dialog提示
     * @param color Int         条目文字颜色
     * @param canCelable Boolean    设置对话框返回键关闭
     * @param confirm 确定回调接口
     */
    @JvmStatic
    fun showBottomOptionDialog(
        context: Context,
        message: String, @ColorRes color: Int = R.color.color_007eff,
        canCelable: Boolean = false,
        confirm: (which: Int) -> Unit
    ) {
        BottomSheetDialog(context)
            .builder()
            .setTitle("提示")
            .setCancelable(canCelable)
            .setCanceledOnTouchOutside(canCelable)
            .addSheetItem(
                message,
                color,
                listener = object : BottomSheetDialog.OnSheetItemClickListener {
                    override fun onClick(which: Int) {
                        confirm(which)
                    }
                })
            .show()
    }
}
