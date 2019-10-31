package com.cs.baselib.base

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.cs.baselib.R
import com.cs.baselib.mvp.IPresenter
import com.cs.baselib.mvp.IView
import kotlinx.android.synthetic.main.activity_base_title.*
import kotlinx.android.synthetic.main.base_toolbar.*

abstract class BaseMvpTitleActivity<in V : IView, P : IPresenter<V>> : BaseMvpActivity<V, P>() {

    protected abstract fun attachChildLayoutRes(): Int

    /**
     * 是否启用返回键
     */
    open fun hasBackIcon(): Boolean = true

    override fun attachLayoutRes(): Int = R.layout.activity_base_title

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        base_container.addView(layoutInflater.inflate(attachChildLayoutRes(), null))
        supportActionBar?.setDisplayHomeAsUpEnabled(hasBackIcon())
    }

    /**
     * 设置 Title
     */
    fun setBaseTitle(title: String) {
        base_title_tv.text = title
    }

    /**
     * 设置 Title
     */
    fun setBaseTitleText(@StringRes resId: Int) {
        base_title_tv.setText(resId)
    }

    /**
     * 设置 Title 颜色
     */
    fun setBaseTitleColor(@ColorRes color: Int) {
        base_title_tv.setTextColor(resources.getColor(color))
    }
}