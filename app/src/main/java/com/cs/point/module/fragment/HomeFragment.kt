package com.dizoo.jixx.ui.home.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.cs.baselib.base.BaseFragment
import com.cs.baselib.ext.click
import com.cs.baselib.ext.load
import com.cs.point.R
import com.cs.point.ext.ALBUM_REQUEST_CODE
import com.cs.point.ext.album
import com.cs.point.ext.compress
import com.cs.point.ext.requestAlbumPermission
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {
    override fun attachLayoutRes(): Int = R.layout.fragment_home


    override fun initView(view: View) {
        btSelect.click {
            activity?.let {
                requestAlbumPermission(it) { album(capture = true) }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ALBUM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var pathResult = Matisse.obtainPathResult(data)
            compress(activity as Context,pathResult[0]){
                it?.let { path -> ivPicture.load(path) }
            }
        }
    }
}