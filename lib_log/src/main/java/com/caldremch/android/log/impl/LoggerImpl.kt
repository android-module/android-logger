package com.caldremch.android.log.impl

import android.util.Log
import com.caldremch.android.log.ILogger

/**
 * Created by Leon on 2022/8/8.
 */
internal class LoggerImpl : ILogger {
    override fun d(tag: String, msg: String?) {
        Log.d(tag, msg ?: "")
    }

    override fun e(tag: String, msg: String?) {
        Log.e(tag, msg ?: "")
    }
}