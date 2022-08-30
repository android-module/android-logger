package com.caldremch.android.log.impl

import com.caldremch.android.log.ILogger

/**
 * Created by Leon on 2022/8/30
 */
class JvmLoggerImpl : ILogger {
    override fun d(tag: String, msg: String?) {
        System.out.println("$tag:$msg")
    }

    override fun e(tag: String, msg: String?) {
        System.err.println("$tag:$msg")
    }

}