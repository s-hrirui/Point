package com.cs.point.http

import com.cs.baselib.http.RetrofitFactory
import com.cs.point.BuildConfig
import com.dizoo.jixx.net.api.ApiService

object RetrofitClient : RetrofitFactory<ApiService>() {

    override fun baseUrl(): String = BuildConfig.BASE_URL

    override fun service(): Class<ApiService> = ApiService::class.java
}