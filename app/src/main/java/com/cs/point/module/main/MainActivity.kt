package com.cs.point.module.main

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cs.baselib.base.BaseActivity
import com.cs.baselib.ext.hideKeyboard
import com.cs.baselib.ext.isVisible
import com.cs.baselib.ext.toast
import com.cs.baselib.util.StatusBarUtil
import com.cs.point.R
import com.cs.point.adapter.MainVpAdapter
import com.cs.point.constant.CURRENT_PAGE
import com.cs.point.ext.listener.onPageSelected
import com.dizoo.jixx.ui.home.fragment.HomeFragment
import com.dizoo.jixx.ui.mine.MineFragment
import com.dizoo.jixx.ui.order.fragment.OrderFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var exitTime: Long = 0
    private var fragments = mutableListOf<Fragment>()

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        setStatusBar()
        hideKeyboard(vpMian)
        initFragment()
        navigationView.itemIconTintList = null
        vpMian.adapter = MainVpAdapter(supportFragmentManager,fragments)
        vpMian.offscreenPageLimit = 3
        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_money -> vpMian.setCurrentItem(0, true)
                R.id.action_home -> if (ivCenter.isVisible) {
                    toast("暂未开发")
                } else {
                    vpMian.setCurrentItem(0, true)
                }
                R.id.action_invite -> vpMian.setCurrentItem(1, true)
                R.id.action_withdraw -> vpMian.setCurrentItem(2, true)
                R.id.action_person -> vpMian.setCurrentItem(3, true)
            }
            return@setOnNavigationItemSelectedListener true
        }
        vpMian.run {
            onPageSelected {
                navigationView.menu.getItem(if (it >= 2) it + 1 else it).isChecked = true
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        val currentPage = intent?.getIntExtra(CURRENT_PAGE, 0)
        currentPage?.let { vpMian.setCurrentItem(it, true) }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast(R.string.main_exit)
            exitTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }


    private fun setStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
        StatusBarUtil.setTranslucentForImageViewInFragment(this@MainActivity, 0,null)
    }

    private fun initFragment() {
        fragments.add(HomeFragment())
        fragments.add(OrderFragment())
        fragments.add(MineFragment())
        fragments.add(MineFragment())
    }
}
