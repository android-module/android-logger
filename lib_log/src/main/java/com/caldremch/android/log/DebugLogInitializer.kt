package com.caldremch.android.log

import com.caldremch.android.log.impl.DefaultLoggerFactoryImpl
import com.caldremch.android.log.impl.DefaultServerLoggerFactoryImpl
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.concurrent.thread

/**
 * Created by Leon on 2022/8/7.
 */
object DebugLogInitializer {

    internal var enable: Boolean = false
    internal var sLogger: ILogger? = null
    internal var sServerLogger: IServerLogger? = null
    internal var enableWebLog = false
    private var detectEnable = true
    private const val port = 34000
    private const val maxPort = 34010
    private lateinit var datagramSocket: DatagramSocket
    private const val PROTOCOL = "C1WL2202208"
    private val lock = Object()
    private var hasInit = false

    @JvmStatic
    fun pauseDetect() {
        if(enableWebLog){
            debugLog { "停止探测" }
            detectEnable = false
        }

    }

    @JvmStatic
    fun initLite(logEnable: Boolean, loggerFactory: ILoggerFactory? = null) {
        if (hasInit) return
        hasInit = true
        enable = logEnable
        enableWebLog = false
        sLogger = (loggerFactory ?: DefaultLoggerFactoryImpl()).create()
    }

    @JvmStatic
    fun resumeDetect() {
        if (enableWebLog.not()) {
            return
        }
        if (detectEnable.not()) {
            debugLog { "恢复探测" }
            synchronized(lock) {
                try {
                    lock.notify()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {

                }
            }
        }
        detectEnable = true
    }

    private fun circleCreateSocket(port: Int): Int {
        debugLog { "检查端口:$port 占用情况" }
        if (port > maxPort) return -1
        try {
            datagramSocket = DatagramSocket(port)
            debugLog { "$port 未占用, 使用当前端口" }
            return port
        } catch (e: Exception) {
            if (e.message?.contains("EADDRINUSE") == true) {
                errorLog { "$port 已占用,使用+1端口" }
                return circleCreateSocket(port + 1)
            }
            throw RuntimeException(e)
        }

    }

    fun setEnable(logEnable: Boolean) {
        this.enable = logEnable
    }

    @JvmStatic
    fun initWithDetect(
        logEnable: Boolean,
        durationMills: Long? = null,
        loggerFactory: ILoggerFactory? = null
    ) {
        if (hasInit) return
        hasInit = true
        enableWebLog = true
        enable = logEnable
        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
        if (enable) {
            thread(true) {
                val finalPort = circleCreateSocket(port)
                debugLog { "最终端口:$finalPort" }
                if (finalPort == -1) throw RuntimeException("logger init error with all port already used")
                thread(true) {
                    while (true) {
                        synchronized(lock) {
                            if (detectEnable.not()) {
                                lock.wait()
                            }
                            Thread.sleep((durationMills ?: 3L) * 1000)
                            val broadcastHost = "255.255.255.255"

                            //向服务器发送请求, 服务器根据请求信息做响应的处理
                            try {
                                val address: InetAddress = InetAddress.getByName(broadcastHost)
                                val datagramPacket =
                                    DatagramPacket(
                                        PROTOCOL.toByteArray(),
                                        PROTOCOL.length,
                                        address,
                                        finalPort
                                    )
                                datagramSocket.send(datagramPacket)
                            } catch (e: Exception) {
                            }
                        }
                    }
                }

                while (true) {
                    /*接收信息*/
                    val data = ByteArray(1024)
                    val packet = DatagramPacket(data, data.size)
                    datagramSocket.receive(packet)
                    val result = String(packet.data, packet.offset, packet.length)
                    if (result.isNotBlank() && result != PROTOCOL) {
                        if (sServerLogger == null) {
                            debugLog { "创建${result}通信通道" }
                            sServerLogger = DefaultServerLoggerFactoryImpl(result).create().apply {
                                debugLog { "执行连接" }
                                connect()
                            }
                        } else if (sServerLogger!!.isConnected().not()) {
                            sServerLogger!!.apply {
                                debugLog { "重置为:${result}通信通道" }
                                sServerLogger?.resetUrl(result)
                            }
                        }
                    }
                }
            }


        }

    }

    //可扩展的api, 暂时不开放
//    @JvmStatic
//    fun init(
//        logEnable: Boolean,
//        websocketUrl: String, //192.168.101.2:8080
//        loggerFactory: ILoggerFactory? = null
//    ) {
//        enable = logEnable
//        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
//        websocketUrl.let {
//            sServerLogger = DefaultServerLoggerFactoryImpl(it).create().apply {
//                connect()
//            }
//        }
//    }


    /**
     * 可扩展的api, 暂时不开放
     */
//    @JvmStatic
//    fun init(
//        logEnable: Boolean,
//        loggerFactory: ILoggerFactory? = null,
//        serverLoggerFactory: IServerLoggerFactory? = null
//    ) {
//        enable = logEnable
//        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
//        sServerLogger =
//            serverLoggerFactory?.create()?.apply {
//                connect()
//            }
//    }
}