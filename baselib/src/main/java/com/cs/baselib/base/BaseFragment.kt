package com.cs.baselib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.cs.baselib.bus.BindBus
import com.cs.baselib.bus.EventData
import com.cs.baselib.bus.RxBus
import com.cs.baselib.bus.RxBusManage
import io.reactivex.disposables.Disposable

abstract class BaseFragment : Fragment() {

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false

    open var bundle :Bundle = Bundle()

    @LayoutRes
    abstract fun attachLayoutRes(): Int

    open fun initData() {}

    abstract fun initView(view: View)

    open fun lazyLoad(){}

    open fun eventResult(event: EventData) {}

    private val mSubscription: Disposable by lazy {
        RxBus.toObservableSticky(EventData::class.java).subscribe{eventResult(it)}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(attachLayoutRes(), null)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this.javaClass.isAnnotationPresent(BindBus::class.java)) {
            RxBusManage.add(mSubscription)
        }
        isViewPrepare = true
        initView(view)
        initData()
        lazyLoadDataIfPrepared()
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this.javaClass.isAnnotationPresent(BindBus::class.java)) {
            RxBusManage.remove(mSubscription)
        }
    }
}