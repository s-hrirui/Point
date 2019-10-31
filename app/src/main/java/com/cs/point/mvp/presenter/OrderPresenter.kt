package com.cs.point.mvp.presenter

import com.cs.baselib.ext.load
import com.cs.baselib.mvp.BasePresenter
import com.cs.point.mvp.contract.OrderContract
import com.cs.point.mvp.model.OrderModel


class OrderPresenter : BasePresenter<OrderContract.Model, OrderContract.View>(),OrderContract.Presenter{

    override fun createModel(): OrderContract.Model? = OrderModel()

    override fun banner() {
        mModel?.banner()?.load(mModel,mView,true,onSuccess = {mView?.bannerResult(it.data)})
    }
}