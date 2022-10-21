package com.caldremch.android.log

import com.caldremch.android.log.inter.DebugLogLevel
import com.caldremch.android.log.inter.PendingMsg
import java.text.SimpleDateFormat
import java.util.Date


/**
 * Created by Leon on 2022/7/10
 */

private const val DEFAULT_TAG = "debugLog"

fun debugLogSimple(l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.d(DEFAULT_TAG, msg)
    }
}

fun errorLogSimple(l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.e(DEFAULT_TAG, msg)
    }
}

private val dataFormat by lazy { SimpleDateFormat("yyyy-MM-dd HH:mm:ss") }

private fun wrapWithLogLevel(level:Int, msg:String, isPending:Boolean = false):String{
    val msgBytes = msg.toByteArray()
    var pendingFlagLen = 0;
    var pendingDate = ""
    if(isPending){
        pendingDate = "[<Pending>${dataFormat.format(Date())}]"
        pendingFlagLen = pendingDate.length
    }
    val levelByteTakeIn = 1
    val sendBytes = ByteArray(levelByteTakeIn+pendingFlagLen+msgBytes.size)
    sendBytes[0] = level.toByte()
    if(pendingFlagLen>0){
        System.arraycopy(pendingDate.toByteArray(), 0, sendBytes, levelByteTakeIn, pendingDate.length)
    }
    System.arraycopy(msgBytes, 0, sendBytes,levelByteTakeIn+pendingDate.length, msgBytes.size)
    return String(sendBytes)
}

fun debugLog(tag: String, l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.d(tag, msg)
        handleSend(DebugLogLevel.DEBUG, msg)
    }
}

fun debugLog(l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.d(DEFAULT_TAG, msg)
        handleSend(DebugLogLevel.DEBUG, msg)
    }
}

private fun handleSend(level: DebugLogLevel, msg: String) {
    val sendMsg = wrapWithLogLevel(level.ordinal, msg, DebugLogInitializer.sServerLogger == null)
    if (DebugLogInitializer.sServerLogger == null && DebugLogInitializer.enableWebLog) {
        PendingMsg.addPending(sendMsg)
    } else {
        DebugLogInitializer.sServerLogger?.send(sendMsg)
    }
}

fun errorLog(l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.e(DEFAULT_TAG, msg)
        handleSend(DebugLogLevel.ERROR, msg)
    }
}


fun errorLog(tag: String, l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.e(tag, msg)
        handleSend(DebugLogLevel.ERROR, msg)
    }
}

fun debugRun(l: () -> Unit) {
    if (DebugLogInitializer.enable) {
        l.invoke()
    }
}