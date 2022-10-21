package com.caldremch.android.logger.demo

import android.app.Application
import com.caldremch.android.log.debugLog

/**
 * Created by Leon on 2022/10/6.
 */
class App : Application() {
    private val TAG ="App"
    override fun onCreate() {
        super.onCreate()
        debugLog { "我一定是pending消息1" }
        debugLog { "我一定是pending消息2" }
    }

}