package com.cs.baselib.http


object HttpConstant {

    const val DEFAULT_TIMEOUT: Long = 30

    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50 // 50M 的缓存大小

    const val TOKEN_KEY = "token"
    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"

    const val SAVE_USER_LOGIN_KEY = "user/login"
    const val SAVE_USER_REGISTER_KEY = "user/register"
    const val REMOVE_USER_LOGOUT_KEY = "user/logout"

}