package com.caldremch.android.logger

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caldremch.android.ble.BleSpeaker
import com.caldremch.android.ble.IBleListener
import com.caldremch.android.log.DebugLogInitializer
import com.caldremch.android.log.debugLog
import com.caldremch.android.log.errorLog
import com.caldremch.android.logger.adapter.BlePairedAdapter
import java.net.DatagramSocket

class MainActivity : AppCompatActivity() {

    private val handlerThread = HandlerThread("service-discovery")
    private lateinit var handler: Handler
    private val datagramSocket by lazy { DatagramSocket() }
    private val cbSupport: CheckBox by lazy { findViewById(R.id.cbSupport) }
    private val cbOpen: CheckBox by lazy { findViewById(R.id.cbOpen) }
    private val rvPaired: RecyclerView by lazy { findViewById(R.id.rvPaired) }
    private val cdLogger: CheckBox by lazy { findViewById(R.id.cdLogger) }
    private val adapter = BlePairedAdapter()
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cbSupport.isChecked = BleSpeaker.isDeviceSupportBle()
        cbOpen.isChecked = BleSpeaker.isOpenBle()
        BleSpeaker.registerListener(object : IBleListener{
            override fun onDisable() {
                cbOpen.isChecked = false
            }

            override fun onEnable() {
                cbOpen.isChecked = true
            }
        })

//        rvPaired.layoutManager = LinearLayoutManager(this)
//        rvPaired.adapter = adapter

        BleSpeaker.getBlePairedDevices()?.forEach {
            debugLog { "蓝牙:${it.name}" }
        }

//        Handler(Looper.getMainLooper()).postDelayed({
//            DebugLogInitializer.setEnable(false)
//        },1000)

        cdLogger.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                DebugLogInitializer.setUpNetLog()
            }else{
                DebugLogInitializer.shutDownNetLog()
            }
        }

    }

    fun log(view: View) {
        debugLog { "log printer debug" }
        errorLog { "log printer error" }
        errorLog { "https://baidu.com" }
    }

    fun openBle(view: View) {
        BleSpeaker.openBleSetting(this)
    }
}