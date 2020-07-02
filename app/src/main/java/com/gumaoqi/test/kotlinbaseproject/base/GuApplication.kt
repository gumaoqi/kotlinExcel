package com.gumaoqi.test.kotlinbaseproject.base

import android.app.Application
import android.content.Context
import com.gumaoqi.test.kotlinbaseproject.tool.C
import com.gumaoqi.test.kotlinbaseproject.tool.L

class GuApplication : Application() {

    companion object {
        private const val TAG = "GuApplication"
        const val loginEffectTime = 1000 * 60 * 60 * 24
        const val maxPicSize = 9740516//选中照片最大大小为6m
        lateinit var context: Context
        val lastClickTime = System.currentTimeMillis()
        val clickEffectTime = 1000 * 1
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        L.i(TAG, "onCreate")
    }

    override fun onTerminate() {
        super.onTerminate()
        L.i(TAG, "onTerminate")
        ActivityCollector.finishAll()
    }
}