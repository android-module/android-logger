package com.caldremch.android.log.impl

import com.caldremch.android.log.IServerLogger
import com.caldremch.android.log.LogWebSocketClient

/**
 * Created by Leon on 2022/8/8.
 */
internal class ServerLoggerImpl(private var uri: String) : IServerLogger {

    private var instance:LogWebSocketClient? = LogWebSocketClient("ws://$uri/websocket")
    override fun getConnectUrl(): String {
        return uri
    }

    override fun send(text: String?) {
        instance?.send(text)
    }

    override fun connect() {
        instance?.connect()
    }

    override fun isConnected(): Boolean {
        return instance?.isOpen?:false
    }

    override fun resetUrl(result: String) {
        uri = result
        instance = LogWebSocketClient("ws://$uri/websocket")
        connect()
    }


}