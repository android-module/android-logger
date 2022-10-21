package com.caldremch.android.log.detector

/**
 * Created by Leon on 2022/10/21.
 */
internal interface INetLogDetector {
    fun start()
    fun stop()
    fun pause()
    fun resume()
}