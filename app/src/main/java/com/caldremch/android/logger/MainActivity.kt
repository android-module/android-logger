package com.caldremch.android.logger

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.caldremch.android.log.debugLog
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    private val handlerThread = HandlerThread("service-discovery")
    private lateinit var handler: Handler
    private val datagramSocket by lazy { DatagramSocket() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        handlerThread.start()
//        handler = Handler(handlerThread.looper)
//        handler.post {
//            while (true) {
//                Thread.sleep(2000)
//                val broadcastHost = "255.255.255.255"
//                val port = 34000
//                val message = "android  app --> broadcastHost"
//                try {
//                    val address: InetAddress = InetAddress.getByName(broadcastHost)
//                    val datagramSocket = DatagramSocket()
//                    val datagramPacket =
//                        DatagramPacket(message.toByteArray(), message.length, address, port)
//                    datagramSocket.send(datagramPacket)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        handler.sendEmptyMessage(0);
    }

    fun log(view: View) {

        debugLog { "log printer" }
    }
}