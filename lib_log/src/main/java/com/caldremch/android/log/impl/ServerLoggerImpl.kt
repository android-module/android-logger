package com.caldremch.android.log.impl

import com.caldremch.android.log.IServerLogger
import com.caldremch.android.log.LogWebSocketClient

/**
 * Created by Leon on 2022/8/8.
 */
internal class ServerLoggerImpl(private val uri: String) : IServerLogger {

    private val instance = LogWebSocketClient(uri)

    override fun send(text: String?) {
        instance.send(text)
    }

    override fun connect() {
        instance.connect()
    }
}