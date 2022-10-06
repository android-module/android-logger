package com.caldremch.android.logger

import android.app.Application
import com.caldremch.android.log.DebugLogInitializer

/**
 * Created by Leon on 2022/10/6.
 */
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        val logUrl = "ws://192.168.101.2:34001/websocket"
        DebugLogInitializer.init(BuildConfig.DEBUG, logUrl)
    }
}