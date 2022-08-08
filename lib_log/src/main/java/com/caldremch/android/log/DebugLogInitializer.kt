package com.caldremch.android.log

import com.caldremch.android.log.impl.DefaultLoggerFactoryImpl
import com.caldremch.android.log.impl.DefaultServerLoggerFactoryImpl

/**
 * Created by Leon on 2022/8/7.
 */
object DebugLogInitializer {

    internal var enable: Boolean = false
    internal var sLogger: ILogger? = null
    internal var sServerLogger: IServerLogger? = null

    @JvmStatic
    fun init(
        logEnable: Boolean,
        websocketUrl: String, //"ws://192.168.101.2:8080/websocket"
        loggerFactory: ILoggerFactory? = null
    ) {
        enable = logEnable
        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
        websocketUrl.let {
            sServerLogger = DefaultServerLoggerFactoryImpl(it).create().apply {
                connect()
            }
        }
    }


    @JvmStatic
    fun init(
        logEnable: Boolean,
        loggerFactory: ILoggerFactory? = null,
        serverLoggerFactory: IServerLoggerFactory? = null
    ) {
        enable = logEnable
        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
        sServerLogger =
            serverLoggerFactory?.create()?.apply {
                connect()
            }
    }
}