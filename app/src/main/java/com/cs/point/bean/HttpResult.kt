package com.cs.point.bean

import com.cs.baselib.bean.BaseBean

data class HttpResult<T>(
    val data: T
) : BaseBean()