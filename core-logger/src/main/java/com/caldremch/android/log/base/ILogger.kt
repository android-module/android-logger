package com.caldremch.android.log.base

/**
 * Created by Leon on 2022/8/7.
 */
interface ILogger {
    fun d(tag: String, msg: String?)
    fun e(tag: String, msg: String?)
}