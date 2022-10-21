package com.caldremch.android.logger

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.caldremch.android.log.DebugLogInitializer
import com.caldremch.android.log.debugLog

/**
 * Created by Leon on 2022/10/21.
 */
internal object LoggerLifeCycleManager {
    fun lifeCycleRegister(){
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                debugLog { "回到前台" }
                onForeground()
            }

            override fun onStop(owner: LifecycleOwner) {
                debugLog { "切到后台" }
                onBackground()
            }
        })
    }

    fun onForeground(){
        DebugLogInitializer.resumeDetect()
    }

    fun onBackground(){
        DebugLogInitializer.pauseDetect()
    }
}