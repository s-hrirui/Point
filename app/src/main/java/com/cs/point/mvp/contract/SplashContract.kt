package com.cs.point.mvp.contract

import com.cs.baselib.mvp.IModel
import com.cs.baselib.mvp.IPresenter
import com.cs.baselib.mvp.IView
import com.cs.point.bean.HttpResult
import com.cs.point.bean.UpdateBean
import io.reactivex.Observable

interface  SplashContract {

    interface View : IView {
        fun checkUpdateResult(vo: UpdateBean)
    }

    interface Presenter : IPresenter<View> {
        fun checkUpdate()
    }

    interface Model : IModel {
        fun checkUpdate(): Observable<HttpResult<UpdateBean>>
    }
}