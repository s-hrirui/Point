package com.cs.point.mvp.model

import com.cs.baselib.mvp.BaseModel
import com.cs.point.bean.HttpResult
import com.cs.point.bean.UpdateBean
import com.cs.point.ext.initMap
import com.cs.point.http.RetrofitClient
import com.cs.point.mvp.contract.SplashContract
import io.reactivex.Observable

class SplashModel : BaseModel(), SplashContract.Model{

    override fun checkUpdate(): Observable<HttpResult<UpdateBean>> = RetrofitClient.service.checkUpdate(initMap())
}