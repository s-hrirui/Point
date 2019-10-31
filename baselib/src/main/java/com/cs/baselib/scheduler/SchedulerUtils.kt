package com.cxz.kotlin.baselibs.rx

import com.cxz.kotlin.baselibs.rx.scheduler.IoMainScheduler


object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> = IoMainScheduler()

}