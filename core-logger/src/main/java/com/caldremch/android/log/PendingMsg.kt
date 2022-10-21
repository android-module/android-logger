package com.caldremch.android.log

import java.util.LinkedList

/**
 * Created by Leon on 2022/10/18.
 */
internal object PendingMsg {

    private val pendingMsg: LinkedList<String> by lazy { LinkedList() }

    fun addPending(msg: String) {
        pendingMsg.add(msg)
    }

    fun flush(client: LogWebSocketClient) {
        val hasPending = pendingMsg.isNotEmpty()
        if (hasPending) {
            debugLog { "存在pending消息, 开始发送pending消息" }
            debugLog { ">>>>>>>>>>>>>>>Pending消息<<<<<<<<<<<<<<<" }
        }
        pendingMsg.forEach {
            client.send(it)
        }
        pendingMsg.clear()
        if (hasPending) {
            debugLog { ">>>>>>>>>>>>>>>Pending消息<<<<<<<<<<<<<<<" }
        }
    }

}