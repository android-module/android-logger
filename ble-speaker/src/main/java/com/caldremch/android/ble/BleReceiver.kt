package com.caldremch.android.ble

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.caldremch.android.log.debugLog

/**
 * Created by Leon on 2022/10/20.
 */
class BleReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED){
            val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
            when(blueState){
                BluetoothAdapter.STATE_TURNING_ON->{
                    debugLog { "打开了" }
                    BleSpeaker.bleStatusListener.forEach {
                        it.onEnable()
                    }
                }
                BluetoothAdapter.STATE_TURNING_OFF->{
                    debugLog { "关闭了" }
                    BleSpeaker.bleStatusListener.forEach {
                        it.onDisable()
                    }
                }
            }
        }
    }
}