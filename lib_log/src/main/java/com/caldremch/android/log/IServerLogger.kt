package com.caldremch.android.log

/**
 * Created by Leon on 2022/8/7.
 */
interface IServerLogger {
    fun send(text: String?)
    fun connect()
}