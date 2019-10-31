package com.cxz.kotlin.baselibs.http.exception

import com.cs.baselib.ext.e
import com.cs.baselib.http.HttpStatus
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

class ExceptionHandle {
    companion object {
        private const val TAG = "ExceptionHandle"
        var errorCode = HttpStatus.UNKNOWN_ERROR
        var errorMsg = "请求失败，请稍后重试"

        fun handleException(e: Throwable): String {
            e.printStackTrace()
            if (e is SocketTimeoutException
                || e is ConnectException
                || e is HttpException
            ) { //均视为网络错误
                "网络连接异常: ${e.message}".e(TAG)
                errorMsg = "网络连接异常"
                errorCode = HttpStatus.NETWORK_ERROR
            } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
            ) {   //均视为解析错误
                "数据解析异常: ${e.message}".e(TAG)
                errorMsg = "数据解析异常"
                errorCode = HttpStatus.SERVER_ERROR
            } else if (e is ApiException) {//服务器返回的错误信息
                errorMsg = e.message.toString()
                errorCode = HttpStatus.SERVER_ERROR
            } else if (e is UnknownHostException) {
                "网络连接异常: ${e.message}".e(TAG)
                errorMsg = "网络连接异常"
                errorCode = HttpStatus.NETWORK_ERROR
            } else if (e is IllegalArgumentException) {
                errorMsg = "参数错误"
                errorCode = HttpStatus.SERVER_ERROR
            } else {//未知错误
                try {
                    "错误: ${e.message}".e(TAG)
                } catch (e1: Exception) {
                    "未知错误Debug调试: ${e.message}".e(TAG)
                }
                errorMsg = "未知错误，可能抛锚了吧~"
                errorCode = HttpStatus.UNKNOWN_ERROR
            }
            return errorMsg
        }

    }
}