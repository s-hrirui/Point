package com.cs.point.app

import android.content.Context
import androidx.multidex.MultiDex
import com.cs.baselib.app.BaseApplication
import kotlin.properties.Delegates

class App :BaseApplication(){

    companion object {
        var instance: Context by Delegates.notNull()
            private set
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun init() {
        instance = this
    }
}