package com.cs.point.mvp.contract

import com.cs.baselib.mvp.IModel
import com.cs.baselib.mvp.IPresenter
import com.cs.baselib.mvp.IView


interface TestContract {

    interface View : IView {

    }

    interface Presenter : IPresenter<View> {

    }

    interface Model : IModel {

    }

}