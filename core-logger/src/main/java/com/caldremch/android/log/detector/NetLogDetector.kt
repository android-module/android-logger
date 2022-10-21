package com.caldremch.android.log.detector

import com.caldremch.android.log.DebugLogInitializer
import com.caldremch.android.log.debugLog
import com.caldremch.android.log.errorLog
import com.caldremch.android.log.impl.DefaultServerLoggerFactoryImpl
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.concurrent.thread

/**
 * Created by Leon on 2022/10/21.
 */
internal class NetLogDetector private constructor() : INetLogDetector {

    interface Factory {
        fun create(): INetLogDetector = NetLogDetector()
    }

    class DefaultFactory : Factory

    private val port = 34000
    private val maxPort = 34010
    private lateinit var datagramSocket: DatagramSocket
    private val PROTOCOL = "C1WL2202208"
    private val lock = Object()
    private var detectEnable = true
    private var durationMills: Long? = null
    private val broadcastHost = "255.255.255.255"
    private var sendPacketDetectFlag = true
    private var receivePacketDetectFlag = true

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
           return -1;
        }

    }

    override fun start() {
        sendPacketDetectFlag = true
        receivePacketDetectFlag = true
        thread(true) {
            val finalPort = circleCreateSocket(port)
            debugLog { "最终端口:$finalPort" }
            if (finalPort == -1) {
                debugLog { "无端口可用, 无法开启探测器" }
                return@thread
            }

            //探测包发送
            thread(true) {
                while (sendPacketDetectFlag) {
                    synchronized(lock) {
                        if (detectEnable.not()) {
                            // TODO: 直接关闭线程的时候, 这里的wait会发生什么?
                            try {
                                lock.wait()
                            }catch (e:InterruptedException){
                                errorLog { "lock.wait()--> ${e.message}" }
                            }
                        }
                        Thread.sleep((durationMills ?: 3L) * 1000)

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
                            errorLog { "socket.send-->${e.message}" }
                        }
                    }
                }
            }

            //探测回复包接收
            while (receivePacketDetectFlag) {
                /*接收信息*/
                val data = ByteArray(1024)
                val packet = DatagramPacket(data, data.size)
                datagramSocket.receive(packet)
                val result = String(packet.data, packet.offset, packet.length)
                if (result.isNotBlank() && result != PROTOCOL) {
                    if (DebugLogInitializer.sServerLogger == null) {
                        debugLog { "创建${result}通信通道" }
                        DebugLogInitializer.sServerLogger =
                            DefaultServerLoggerFactoryImpl(result).create().apply {
                                debugLog { "执行连接" }
                                connect()
                            }
                    } else if (DebugLogInitializer.sServerLogger!!.isConnected().not()) {
                        DebugLogInitializer.sServerLogger!!.apply {
                            debugLog { "重置为:${result}通信通道" }
                            DebugLogInitializer.sServerLogger?.resetUrl(result)
                        }
                    }
                }
            }

            //关闭socket
            datagramSocket.close()
            datagramSocket.disconnect()
            debugLog { "成功关闭探测器" }
        }
    }

    override fun stop() {
        debugLog { "开始关闭探测器..." }
        sendPacketDetectFlag = false
        receivePacketDetectFlag = false
    }

    override fun pause() {
        if (DebugLogInitializer.enableWebLog.not() && sendPacketDetectFlag.not() && receivePacketDetectFlag.not()) {
            debugLog { "停止探测" }
            detectEnable = false
        }
    }

    override fun resume() {
        if (DebugLogInitializer.enableWebLog.not() && sendPacketDetectFlag.not() && receivePacketDetectFlag.not()) {
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

}