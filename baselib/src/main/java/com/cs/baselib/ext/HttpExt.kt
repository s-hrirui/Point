package com.cs.baselib.ext

import com.cs.baselib.R
import com.cs.baselib.app.BaseApplication
import com.cs.baselib.bean.BaseBean
import com.cs.baselib.http.HttpStatus
import com.cs.baselib.mvp.IModel
import com.cs.baselib.mvp.IView
import com.cxz.kotlin.baselibs.http.exception.ExceptionHandle
import com.cxz.kotlin.baselibs.http.function.RetryWithDelay
import com.cxz.kotlin.baselibs.rx.SchedulerUtils
import com.dizoo.jixx.util.NetWorkUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

fun <T : BaseBean> Observable<T>.load(
    model: IModel?,
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit,
    onError: ((T) -> Unit)? = null
) {
    this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe(object : Observer<T> {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onSubscribe(d: Disposable) {
                if (isShowLoading) view?.showLoading()
                model?.addDisposable(d)
                if (!NetWorkUtil.isConnected()) {
                    view?.showDefaultMsg(BaseApplication.instance.getString(R.string.network_error))
                    d.dispose()
                    onComplete()
                }
            }

            override fun onNext(t: T) {
                view?.hideLoading()
                when {
                    t.errorCode == HttpStatus.SUCCESS -> onSuccess.invoke(t)
                    t.errorCode == HttpStatus.TOKEN_INVALID -> {
                        // Token 过期，重新登录
                    }
                    else -> {
                        if (onError != null) {
                            onError.invoke(t)
                        } else {
                            if (t.errorMsg.isNotEmpty())
                                view?.showDefaultMsg(t.errorMsg)
                        }
                    }
                }
            }

            override fun onError(t: Throwable) {
                view?.hideLoading()
                view?.showError(ExceptionHandle.handleException(t))
            }
        })
}

fun <T : BaseBean> Observable<T>.load(
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit,
    onError: ((T) -> Unit)? = null
): Disposable {
    if (isShowLoading) view?.showLoading()
    return this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe({
            view?.hideLoading()
            when {
                it.errorCode == HttpStatus.SUCCESS -> onSuccess.invoke(it)
                it.errorCode == HttpStatus.TOKEN_INVALID -> {
                    // Token 过期，重新登录
                }
                else -> {
                    if (onError != null) {
                        onError.invoke(it)
                    } else {
                        if (it.errorMsg.isNotEmpty())
                            view?.showDefaultMsg(it.errorMsg)
                    }
                }
            }
        }, {
            view?.hideLoading()
            view?.showError(ExceptionHandle.handleException(it))
        })
}


