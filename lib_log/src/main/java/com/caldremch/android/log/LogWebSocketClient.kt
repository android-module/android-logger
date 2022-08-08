package com.caldremch.android.log

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

/**
 * Created by Leon on 2022/7/22
 * ws://192.168.101.2:8080/websocket
 * ws://10.30.107.119:8080/websocket
 */
internal class LogWebSocketClient(private val uri: String) : WebSocketClient(URI(uri)) {

    var open = false

    override fun onOpen(handshakedata: ServerHandshake?) {
        open = true
        debugLogSimple { "onMessage $open" }

    }

    override fun onMessage(message: String?) {
        debugLogSimple { "onMessage = $message" }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        open = false
        debugLogSimple { "code = $code, reason=$reason" }
    }

    override fun onError(ex: Exception?) {
        open = false
        ex?.printStackTrace()
    }

    override fun send(text: String?) {
        if (open) {
            super.send(text)
        }
    }

}