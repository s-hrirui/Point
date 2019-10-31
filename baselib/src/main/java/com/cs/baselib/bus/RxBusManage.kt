package com.cs.baselib.bus

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

object RxBusManage {

    private val mSubscription = CompositeDisposable()

    val isDisposed: Boolean
        get() = mSubscription.isDisposed

    fun add(s: Disposable?) {
        if (s != null) {
            mSubscription.add(s)
        }
    }

    fun remove(s: Disposable?) {
        if (s != null) {
            mSubscription.remove(s)
        }
    }

    fun clear() {
        mSubscription.clear()
    }

    fun dispose() {
        mSubscription.dispose()
    }
}