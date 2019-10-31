package com.cs.point.mvp.model

import com.cs.baselib.mvp.BaseModel
import com.cs.point.bean.BannerBean
import com.cs.point.bean.HttpResult
import com.cs.point.http.RetrofitClient
import com.cs.point.mvp.contract.OrderContract
import io.reactivex.Observable

class OrderModel : BaseModel(), OrderContract.Model{

    override fun banner(): Observable<HttpResult<MutableList<BannerBean>>> = RetrofitClient.service.banner()
}