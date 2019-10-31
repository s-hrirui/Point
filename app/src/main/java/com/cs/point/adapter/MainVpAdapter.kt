package com.cs.point.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

class MainVpAdapter : FragmentPagerAdapter {

    private var mFragments: List<Fragment>? = null
    private var listTitle = listOf<String>()

    constructor(fm: FragmentManager, mFragments: List<Fragment>) : super(fm) {
        this.mFragments = mFragments
    }


    override fun getItem(position: Int): Fragment {
        return mFragments!![position]
    }

    override fun getCount(): Int {
        return mFragments!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

    override fun getItemPosition(any: Any): Int  = PagerAdapter.POSITION_NONE

    fun recreateItems(fragmentList: List<Fragment>, titleList: List<String>) {
        this.mFragments = fragmentList
        this.listTitle = titleList
        notifyDataSetChanged()
    }
}