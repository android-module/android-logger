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

    @JvmStatic
    fun pauseDetect() {
        detectEnable = false
    }

    @JvmStatic
    fun initLite(logEnable: Boolean, loggerFactory: ILoggerFactory? = null) {
        enable = logEnable
        enableWebLog = false
        sLogger = (loggerFactory ?: DefaultLoggerFactoryImpl()).create()
    }

    @JvmStatic
    fun resumeDetect() {
        if (detectEnable.not()) {
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
        System.err.println("检查端口:$port 占用情况")
        if (port > maxPort) return -1
        try {
            datagramSocket = DatagramSocket(port)
            System.err.println("$port 未占用")
            return port
        } catch (e: Exception) {
            if (e.message?.contains("EADDRINUSE") == true) {
                System.err.println("$port 已占用")
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
        enableWebLog = true
        enable = logEnable
        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
        if (enable) {
            thread(true) {
                val finalPort = circleCreateSocket(port)
                System.err.println("最终端口:$finalPort")
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
                            System.err.println("创建IServerLogger实例-->$result")
                            sServerLogger = DefaultServerLoggerFactoryImpl(result).create().apply {
                                System.err.println("IServerLogger执行连接")
                                connect()
                            }
                        } else if (sServerLogger!!.isConnected().not()) {
                            sServerLogger!!.apply {
                                System.err.println("IServerLogger执行重置并连接")
                                sServerLogger?.resetUrl(result)
                            }
                        }
                    }
                }
            }


        }

    }

    @JvmStatic
    fun init(
        logEnable: Boolean,
        websocketUrl: String, //192.168.101.2:8080
        loggerFactory: ILoggerFactory? = null
    ) {
        enable = logEnable
        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
        websocketUrl.let {
            sServerLogger = DefaultServerLoggerFactoryImpl(it).create().apply {
                connect()
            }
        }
    }


    @JvmStatic
    fun init(
        logEnable: Boolean,
        loggerFactory: ILoggerFactory? = null,
        serverLoggerFactory: IServerLoggerFactory? = null
    ) {
        enable = logEnable
        sLogger = loggerFactory?.create() ?: DefaultLoggerFactoryImpl().create()
        sServerLogger =
            serverLoggerFactory?.create()?.apply {
                connect()
            }
    }
}