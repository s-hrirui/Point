package com.cs.point.mvp.contract

import com.cs.baselib.mvp.IModel
import com.cs.baselib.mvp.IPresenter
import com.cs.baselib.mvp.IView
import com.cs.point.bean.BannerBean
import com.cs.point.bean.HttpResult
import io.reactivex.Observable

interface  OrderContract {

    interface View : IView {
        fun bannerResult(vo: MutableList<BannerBean>)
    }

    interface Presenter : IPresenter<View> {
        fun banner()
    }

    interface Model : IModel {
        fun banner(): Observable<HttpResult<MutableList<BannerBean>>>
    }
}