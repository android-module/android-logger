package com.caldremch.android.logger

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.caldremch.android.log.DebugLogInitializer
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
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver{

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                Log.d(TAG, "onResume: ")
                onForeground()
            }
            
            override fun onStop(owner: LifecycleOwner) {
                Log.d(TAG, "onStop: ")
                onBackground()
            }
        })
//        val logUrl = "ws://192.168.101.2:34001/websocket"
        DebugLogInitializer.initWithDetect(true)
    }
    fun onForeground(){
        DebugLogInitializer.resumeDetect()
    }

    fun onBackground(){
        DebugLogInitializer.pauseDetect()
    }
}