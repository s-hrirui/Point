package com.cs.baselib.util

import android.app.Activity
import java.util.*
import java.lang.Class


object ActManager {

    private val mActivityList = LinkedList<Activity>()

    val currentActivity: Activity?
        get() =
            if (mActivityList.isEmpty()) null
            else mActivityList.last


    fun addActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        } else {
            mActivityList.add(activity)
        }
    }

    fun removeActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    fun finishCurrentActivity() {
        currentActivity?.finish()
    }

    fun finishActivity(activity: Activity) {
        mActivityList.remove(activity)
        activity.finish()
    }

    fun  finishActivity(clazz: Class<*>){
        for (activity in mActivityList)
            if (activity.javaClass == clazz)
                activity.finish()
    }

    fun finishAllActivity() {
        for (activity in mActivityList)
            activity.finish()
    }
}