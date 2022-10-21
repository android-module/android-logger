package com.caldremch.android.log.inter

import com.caldremch.android.log.debugLog
import com.caldremch.android.log.debugLogSimple
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

/**
 * Created by Leon on 2022/7/22
 * ws://192.168.101.2:8080/websocket
 */
internal class LogWebSocketClient(private val uri: String) : WebSocketClient(URI(uri)) {

    var open = false

    override fun onOpen(handshakedata: ServerHandshake?) {
        open = true
        debugLog { "网络日志已启动完毕, 当前连接:${uri}" }
        //发送pending的信息
        PendingMsg.flush(this)
    }

    override fun onMessage(message: String?) {
        debugLogSimple { "onMessage = $message" }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        open = false
//        debugLogSimple { "onClose code = $code, reason=$reason" }
    }

    override fun onError(ex: Exception?) {
        open = false
//        errorLogSimple { "onError code = ${ex?.message}" }
//        ex?.printStackTrace()
    }

    override fun send(text: String?) {
        if (open) {
            super.send(text)
        }else{
//            try {
//                reconnect()
//            }catch (e:Exception){
//                errorLogSimple { "send ${e.message}" }
//            }
        }
    }

}