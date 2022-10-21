package com.caldremch.android.log.impl

import com.caldremch.android.log.IServerLogger
import com.caldremch.android.log.IServerLoggerFactory

/**
 * Created by Leon on 2022/8/8.
 */
internal class DefaultServerLoggerFactoryImpl(private val url: String) : IServerLoggerFactory {

    override fun create(): IServerLogger {
        return ServerLoggerImpl(url)
    }
}