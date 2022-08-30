package com.caldremch.android.log


/**
 * Created by Leon on 2022/7/10
 */

private const val DEFAULT_TAG = "debugLog"

fun debugLogSimple(l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        Log.d(DEFAULT_TAG, msg)
    }
}

fun debugLog(tag: String, l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.d(tag, msg)
        DebugLogInitializer.sServerLogger?.send(msg)
    }
}

fun debugLog(l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.d(DEFAULT_TAG, msg)
        DebugLogInitializer.sServerLogger?.send(msg)
    }
}

fun errorLog(l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.e(DEFAULT_TAG, msg)
        DebugLogInitializer.sServerLogger?.send(msg)
    }
}


fun errorLog(tag: String, l: () -> String?) {
    if (DebugLogInitializer.enable) {
        val msg = l.invoke() ?: ""
        DebugLogInitializer.sLogger?.e(tag, msg)
        DebugLogInitializer.sServerLogger?.send(msg)
    }
}

fun debugRun(l: () -> Unit) {
    if (DebugLogInitializer.enable) {
        l.invoke()
    }
}