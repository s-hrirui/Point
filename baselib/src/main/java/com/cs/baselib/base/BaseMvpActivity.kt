package com.cs.baselib.base

import android.os.Bundle
import com.cs.baselib.ext.toast
import com.cs.baselib.mvp.IPresenter
import com.cs.baselib.mvp.IView

abstract class BaseMvpActivity<in V : IView, P : IPresenter<V>> : BaseActivity(), IView {

    open var mPresenter: P? = null

    open abstract fun createPresenter(): P

    override fun initView(savedInstanceState: Bundle?) {
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
        this.mPresenter = null
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
        toast(errorMsg)
    }

    override fun showDefaultMsg(msg: String) {
        toast(msg)
    }

    override fun showMsg(msg: String) {
        toast(msg)
    }
}