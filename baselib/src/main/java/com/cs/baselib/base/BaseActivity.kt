package com.cs.baselib.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.cs.baselib.bus.BindBus
import com.cs.baselib.bus.EventData
import com.cs.baselib.bus.RxBus
import com.cs.baselib.bus.RxBusManage
import com.dizoo.jixx.util.KeyBoardUtil
import io.reactivex.disposables.Disposable

abstract class BaseActivity : AppCompatActivity()  {

    open var bundle :Bundle = Bundle()

    @LayoutRes
    protected abstract fun attachLayoutRes(): Int

    open fun initData() {}

    open fun eventResult(event: EventData) {}

    abstract fun initView(savedInstanceState: Bundle?)

    private val mSubscription: Disposable by lazy {
        RxBus.toObservableSticky(EventData::class.java).subscribe{eventResult(it)}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // 强制竖屏
        setContentView(attachLayoutRes())
        if (this.javaClass.isAnnotationPresent(BindBus::class.java)) {
            RxBusManage.add(mSubscription)
        }
        initView(savedInstanceState)
        initData()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            // 如果不是落在EditText区域，则需要关闭输入法
            if (KeyBoardUtil.isHideKeyboard(v, ev)) {
                KeyBoardUtil.hideKeyBoard(this, v)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this.javaClass.isAnnotationPresent(BindBus::class.java)) {
            RxBusManage.remove(mSubscription)
        }
        KeyBoardUtil.fixInputMethodManagerLeak(this)
    }
}