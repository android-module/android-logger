package com.caldremch.android.log

import com.caldremch.android.log.base.ILogger
import com.caldremch.android.log.base.ILoggerFactory
import com.caldremch.android.log.base.IServerLogger
import com.caldremch.android.log.detector.INetLogDetector
import com.caldremch.android.log.detector.NetLogDetector
import com.caldremch.android.log.impl.DefaultLoggerFactoryImpl

/**
 * Created by Leon on 2022/8/7.
 */
object DebugLogInitializer {

    internal var enable: Boolean = false
    internal var sLogger: ILogger? = null
    internal var sServerLogger: IServerLogger? = null
    internal var enableWebLog = false
    private var hasInit = false
    private val netLogDetector: INetLogDetector by lazy { NetLogDetector.DefaultFactory().create() }



    @JvmStatic
    fun pauseDetect() {
        if (enable) {
            netLogDetector.pause()
        }
    }

    @JvmStatic
    fun initLite(logEnable: Boolean, loggerFactory: ILoggerFactory? = null) {
        if (hasInit) return
        hasInit = true
        enable = logEnable
        enableWebLog = false
        sLogger = (loggerFactory ?: DefaultLoggerFactoryImpl()).create()
    }

    @JvmStatic
    fun resumeDetect() {
        if (enable) {
            netLogDetector.resume()
        }
    }

    @JvmStatic
    fun setEnable(logEnable: Boolean,  enableWebLog:Boolean = true) {
        if (this.enable && logEnable.not()) {
            this.enable = false
            //关闭操作
            shutDownNetLog()
        } else if (this.enable.not() && logEnable) {
            this.enable = true
            //开启所有
            setUpNetLog()
        }
    }

    @JvmStatic
    fun initWithDetect(
        logEnable: Boolean,
        loggerFactory: ILoggerFactory? = null
    ) {
        if (hasInit) return
        hasInit = true
        enableWebLog = true
        enable = logEnable
        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
        if (enable) {
            netLogDetector.start()
        }
    }

    @JvmStatic
    fun setUpNetLog() {
        if (enable) {
            netLogDetector.start()
        }
    }

    @JvmStatic
    fun shutDownNetLog() {
        if (enable) {
            netLogDetector.stop()
        }
    }

    //可扩展的api, 暂时不开放
//    @JvmStatic
//    fun init(
//        logEnable: Boolean,
//        websocketUrl: String, //192.168.101.2:8080
//        loggerFactory: ILoggerFactory? = null
//    ) {
//        enable = logEnable
//        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
//        websocketUrl.let {
//            sServerLogger = DefaultServerLoggerFactoryImpl(it).create().apply {
//                connect()
//            }
//        }
//    }


    /**
     * 可扩展的api, 暂时不开放
     */
//    @JvmStatic
//    fun init(
//        logEnable: Boolean,
//        loggerFactory: ILoggerFactory? = null,
//        serverLoggerFactory: IServerLoggerFactory? = null
//    ) {
//        enable = logEnable
//        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
//        sServerLogger =
//            serverLoggerFactory?.create()?.apply {
//                connect()
//            }
//    }
}