package com.dizoo.jixx.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cs.baselib.base.BaseActivity
import com.cs.baselib.util.StatusBarUtil
import com.cs.point.R
import com.cs.point.constant.BUNDLE_RESULT
import com.cs.point.constant.CONTAINER_FRAGMENT
import java.lang.ref.WeakReference


class ContainerActivity : BaseActivity() {

    private val tag = "content_fragment_tag"
    private var mFragment: WeakReference<Fragment>? = null

    override fun attachLayoutRes(): Int = R.layout.activity_container

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarUtil.setTranslucent(this@ContainerActivity, 0)
        StatusBarUtil.setStatusBarLightMode(this@ContainerActivity)
        val fm = supportFragmentManager
        var fragment: Fragment? = null
        if (savedInstanceState != null) {
            fragment = fm.getFragment(savedInstanceState, tag)
        }
        if (fragment == null) {
            fragment = initFromIntent(intent)
        }
        val trans = supportFragmentManager
                .beginTransaction()
        trans.replace(R.id.container, fragment!!)
        trans.commitAllowingStateLoss()
        mFragment = WeakReference(fragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, tag, mFragment?.get()!!)
    }

    private fun initFromIntent(data: Intent?): Fragment {
        if (data == null) {
            throw RuntimeException(
                    "you must provide a page info to display")
        }
        try {
            val fragmentName = data.getStringExtra(CONTAINER_FRAGMENT)
            if (fragmentName == null || "" == fragmentName) {
                throw IllegalArgumentException("can not find page fragmentName")
            }
            val fragmentClass = Class.forName(fragmentName)
            val fragment = fragmentClass.newInstance() as Fragment
            val args = data.getBundleExtra(BUNDLE_RESULT)
            if (args != null) {
                fragment.arguments = args
            }
            return fragment
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        throw RuntimeException("fragment initialization failed!")
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
//        if (fragment is WebFragment) {
//            fragment.onBackPressed()
//        } else {
//            super.onBackPressed()
//        }
    }

}