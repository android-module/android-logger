package com.caldremch.android.log

/**
 * Created by Leon on 2022/8/7.
 */
interface IServerLoggerFactory {
    fun create(): IServerLogger
}