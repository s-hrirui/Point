package com.dizoo.jixx.ui.splash

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.cs.baselib.base.BaseMvpActivity
import com.cs.baselib.ext.e
import com.cs.baselib.ext.postDelay
import com.cs.baselib.ext.startKtxActivityKill
import com.cs.baselib.ext.toast
import com.cs.point.R
import com.cs.point.bean.UpdateBean
import com.cs.point.constant.SystemInfo
import com.cs.point.module.main.MainActivity
import com.cs.point.mvp.contract.SplashContract
import com.cs.point.mvp.presenter.SplashPresenter
import com.dizoo.jixx.util.DialogUtil
import luyao.util.ktx.ext.permission.PermissionRequest
import luyao.util.ktx.ext.permission.requestPermission


class SplashActivity : BaseMvpActivity<SplashContract.View, SplashContract.Presenter>(),SplashContract.View {

    override fun createPresenter(): SplashContract.Presenter = SplashPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        checkTaskRoot()
        requestPermission(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            callbacks = {
                onGranted { systemInfo() }
                onDenied { finish() }
                onShowRationale { show(it) }
                onNeverAskAgain {
                    if (it.isNotEmpty() && it.contains("android.permission.READ_PHONE_STATE")) {
                        toast("${getString(R.string.app_name)}需要开启读取电话权限，您是否允许？在设置-应用-${getString(R.string.app_name)}-权限中开启电话权限，以正常读取设备码")
                        finish()
                    } else {
                        postDelay{ startKtxActivityKill<MainActivity>() }
                    }
                }
            }
        )
    }

    override fun checkUpdateResult(vo: UpdateBean) {
        vo.toString().e()
        toast("ddd")
    }

    private fun systemInfo(){
        val imei1 = SystemInfo.getImei1()
        val imei2 = SystemInfo.getImei2()
        val meid = SystemInfo.getMeId()
        if (TextUtils.isEmpty(meid) && TextUtils.isEmpty(imei1) && TextUtils.isEmpty(imei2)) {
            toast("读取设备识别码异常，请检查是否开启读取设备码权限")
            finish()
            return
        }
//        mPresenter?.checkUpdate()
        postDelay { startKtxActivityKill<MainActivity>() }
    }


    private fun checkTaskRoot() {
        if (!this.isTaskRoot) { // 当前类不是该Task的根部，那么之前启动
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) { // 当前类是从桌面启动的
                    finish() // finish掉该类，直接打开该Task中现存的Activity
                    return
                }
            }
        }
    }


    private fun show(it: PermissionRequest) {
        DialogUtil.showCommonDoubleOptionDialog(
            this@SplashActivity,
            "提示",
            "权限被拒绝",
            confirm = {
                it.permissionFragment.requestPermissionsByFragment(
                    it.permissions.toTypedArray(),
                    it.requestCode
                )
            }, cancel = {
                finish()
            })
    }

}