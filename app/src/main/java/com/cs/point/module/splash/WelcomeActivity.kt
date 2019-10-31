package com.dizoo.jixx.ui.splash

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.cs.baselib.base.BaseActivity
import com.cs.baselib.util.StatusBarUtil
import com.cs.point.R
import com.dizoo.jixx.ui.splash.adapter.WelcomePagerAdatper
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : BaseActivity() {

    private var list = mutableListOf<View>()
    private var dot1: ImageView? = null
    private var dot2: ImageView? = null
    private var dot3: ImageView? = null
    private var distance: Int = 0
    private var isLastPage = false
    private var isDragPage = false
    private var canJumpPage = true

    override fun attachLayoutRes(): Int = R.layout.activity_welcome

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarUtil.setTranslucent(this@WelcomeActivity, 0)
        StatusBarUtil.setStatusBarLightMode(this@WelcomeActivity)
        addPage()
        viewpager.adapter = WelcomePagerAdatper(list)
        addDots()
        moveDots()
        bt_enter.setOnClickListener { finish() }
    }


    private fun addDots() {
        dot1 = ImageView(this@WelcomeActivity)
        dot2 = ImageView(this@WelcomeActivity)
        dot3 = ImageView(this@WelcomeActivity)
        dot1?.setImageResource(R.drawable.shape_welcome_gray_dot)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 40, 0)
        ll_dots.addView(dot1, layoutParams)
        dot2?.setImageResource(R.drawable.shape_welcome_gray_dot)
        ll_dots.addView(dot2, layoutParams)
        dot3?.setImageResource(R.drawable.shape_welcome_gray_dot)
        ll_dots.addView(dot3, layoutParams)
        rl_dot.setPadding(40, 0, 0, 0)
        dot1?.setOnClickListener { viewpager.currentItem = 0 }
        dot2?.setOnClickListener { viewpager.currentItem = 1 }
        dot3?.setOnClickListener { viewpager.currentItem = 2 }
    }

    private fun moveDots() {
        im_light_dot.viewTreeObserver.addOnGlobalLayoutListener {
            //获得两个圆点之间的距离
            distance = ll_dots.getChildAt(1).left - ll_dots.getChildAt(0).left
            im_light_dot.viewTreeObserver.removeOnDrawListener { this@WelcomeActivity }
        }
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
                var leftMargin = distance * (position + positionOffset).toFloat()
                val params = im_light_dot.layoutParams as RelativeLayout.LayoutParams
                params.leftMargin = leftMargin.toInt()
                im_light_dot.layoutParams = params
                //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
                if (isLastPage && isDragPage && positionOffsetPixels == 0){
                    if (canJumpPage){
                        canJumpPage = false
                        finish()
                    }
                }
                if (isDragPage){
                    bt_enter.visibility = View.VISIBLE
                }
            }

            override fun onPageSelected(position: Int) {
                //页面跳转时，设置小圆点的margin
                val leftMargin = (distance * position).toFloat()
                val params = im_light_dot.layoutParams as RelativeLayout.LayoutParams
                params.leftMargin = leftMargin.toInt()
                im_light_dot.layoutParams = params
                when (position) {
                    0 -> rl_welcome.setBackgroundResource(R.drawable.welcome_bg)
                    1 -> rl_welcome.setBackgroundResource(R.drawable.welcome_bg)
                    2 -> rl_welcome.setBackgroundResource(R.drawable.welcome_bg)
                }
                isLastPage = position == list.size -1
            }

            override fun onPageScrollStateChanged(state: Int) {
                isDragPage = state == 1
            }
        })
    }



    private fun addPage() {
        list.add(ImageView(this@WelcomeActivity))
        list.add(ImageView(this@WelcomeActivity))
        list.add(ImageView(this@WelcomeActivity))
    }
}
