package com.caldremch.android.log.inter

import java.net.InetAddress
import java.net.Socket

/**
 * Created by Leon on 2022/10/7.
 *
 * https://blog.csdn.net/chen6021550215he/article/details/107371685
 *
 * 不适合局域网的判断
 */
internal object PortUtils {

    fun isPortAlReadyUse(port:Int):Boolean{
        return isPortAlReadyUse("127.0.0.1", port)
    }

    fun isPortAlReadyUse(host:String, port:Int):Boolean{
        var isUsing = false
        val inetAddress = InetAddress.getByName(host);
        try{
            val socket = Socket(inetAddress, port)
            isUsing = true
            socket.close()
        }catch(e:Exception){
            System.err.println("未被占用:${e.message}")
            e.printStackTrace()
        }
        return isUsing
    }
}