package com.caldremch.android.log.impl

import com.caldremch.android.log.ILogger
import java.lang.reflect.Method

/**
 * Created by Leon on 2022/8/8.
 */
internal class AndroidLoggerImpl : ILogger {

    var methodD: Method? = null
    var methodE: Method? = null

    init {
        val clz = Class.forName("android.util.Log")
        methodD = clz.getMethod("d", String::class.java, String::class.java)
        methodE = clz.getMethod("e", String::class.java, String::class.java)
    }

    override fun d(tag: String, msg: String?) {
        methodD?.invoke(null, tag, msg ?: "")
    }

    override fun e(tag: String, msg: String?) {
        methodE?.invoke(null, tag, msg ?: "")
    }
}