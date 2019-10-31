package com.dizoo.jixx.ui.splash.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter


class WelcomePagerAdatper(private val list: MutableList<View>) : PagerAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(list[position])
        return list[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(list[position])
    }
}
