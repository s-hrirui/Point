package com.cs.point.mvp.presenter

import com.cs.baselib.mvp.BasePresenter
import com.cs.point.mvp.contract.TestContract
import com.cs.point.mvp.model.TestModel


class TestPresenter : BasePresenter<TestContract.Model, TestContract.View>(), TestContract.Presenter {

    override fun createModel(): TestContract.Model? = TestModel()

}