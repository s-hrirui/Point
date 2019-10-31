package com.cs.point.mvp.presenter

import com.cs.baselib.ext.load
import com.cs.baselib.mvp.BasePresenter
import com.cs.point.mvp.contract.SplashContract
import com.cs.point.mvp.model.SplashModel


class SplashPresenter : BasePresenter<SplashContract.Model, SplashContract.View>(),SplashContract.Presenter{

    override fun createModel(): SplashContract.Model? = SplashModel()

    override fun checkUpdate() {
        mModel?.checkUpdate()?.load(mModel,mView,true,onSuccess = {mView?.checkUpdateResult(it.data)})

    }
}