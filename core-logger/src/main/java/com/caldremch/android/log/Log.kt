package com.caldremch.android.log

/**
 * Created by Leon on 2022/8/8.
 *
 * for java
 */
object Log {

    @JvmStatic
    fun d(msg: String?) {
        debugLog { msg }
    }

    @JvmStatic
    fun e(msg: String?) {
        errorLog { msg }
    }

    @JvmStatic
    fun d(tag: String, msg: String?) {
        debugLog(tag) { msg }
    }

    @JvmStatic
    fun e(tag: String, msg: String?) {
        errorLog(tag) { msg }
    }

}