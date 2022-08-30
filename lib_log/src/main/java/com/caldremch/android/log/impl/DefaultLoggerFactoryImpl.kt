package com.caldremch.android.log.impl

import com.caldremch.android.log.ILogger
import com.caldremch.android.log.ILoggerFactory

/**
 * Created by Leon on 2022/8/7.
 */
internal class DefaultLoggerFactoryImpl : ILoggerFactory {
    override fun create(): ILogger {
        if(Platform.isAndroid){
            return AndroidLoggerImpl()
        }
        return JvmLoggerImpl()
    }
}