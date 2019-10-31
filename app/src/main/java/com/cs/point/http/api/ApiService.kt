package com.dizoo.jixx.net.api

import com.cs.point.bean.BannerBean
import com.cs.point.bean.HttpResult
import com.cs.point.bean.UpdateBean
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {


    @GET("banner/json")
    fun banner():Observable<HttpResult<MutableList<BannerBean>>>

    @FormUrlEncoded
    @POST("v2/config")
    fun checkUpdate(@FieldMap map: Map<String, String>): Observable<HttpResult<UpdateBean>> //应用更新
}