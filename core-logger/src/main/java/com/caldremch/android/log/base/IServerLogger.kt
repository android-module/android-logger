package com.caldremch.android.log.base

/**
 * Created by Leon on 2022/8/7.
 */
interface IServerLogger {

    fun getConnectUrl():String
    fun send(text: String?)
    fun connect()

    fun isConnected():Boolean
    fun resetUrl(result: String)
}