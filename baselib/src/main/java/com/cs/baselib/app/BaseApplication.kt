package com.cs.baselib.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.cs.baselib.util.ActManager
import kotlin.properties.Delegates

open class BaseApplication :Application(){

    companion object {
        var instance: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        init()
    }

    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            ActManager.addActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            ActManager.removeActivity(activity)
        }
    }

    open fun init(){}
}