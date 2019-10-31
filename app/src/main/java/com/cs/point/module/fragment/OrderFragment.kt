package com.dizoo.jixx.ui.order.fragment

import android.view.View
import com.cs.baselib.base.BaseMvpFragment
import com.cs.baselib.ext.toast
import com.cs.point.R
import com.cs.point.bean.BannerBean
import com.cs.point.mvp.contract.OrderContract
import com.cs.point.mvp.presenter.OrderPresenter
import com.cs.point.widget.GlideImageLoader
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : BaseMvpFragment<OrderContract.View, OrderContract.Presenter>(), OrderContract.View {

    override fun attachLayoutRes(): Int = R.layout.fragment_order

    override fun createPresenter(): OrderContract.Presenter = OrderPresenter()

    private var list = mutableListOf<String>()

    override fun initView(view: View) {
        super.initView(view)
        banner.setImageLoader(GlideImageLoader()).isAutoPlay(true)
        btBanner.setOnClickListener { mPresenter?.banner() }
    }
    override fun bannerResult(vo: MutableList<BannerBean>) {
        vo.forEach {
            list.add(it.imagePath)
        }
       banner.setImages(list).start()
    }







}