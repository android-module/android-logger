package com.caldremch.android.log

import java.util.LinkedList

/**
 * Created by Leon on 2022/10/18.
 */
internal object PendingMsg {

    private val pendingMsg:LinkedList<String> by lazy { LinkedList() }

     fun addPending(msg:String){
          pendingMsg.add(msg)
     }

     fun flush(client:LogWebSocketClient){
         pendingMsg.forEach {
             client.send(it)
         }
         pendingMsg.clear()
     }

}