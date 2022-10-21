package com.caldremch.android.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * Created by Leon on 2022/10/20.
 *
 * doc: https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#kotlin
 */
object BleSpeaker {


    internal val bleStatusListener by lazy { arrayListOf<IBleListener>() }

    private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }

    fun registerListener(listener: IBleListener){
        bleStatusListener.add(listener)
    }

    fun unRegisterListener(listener: IBleListener){
        bleStatusListener.remove(listener)
    }

    fun isDeviceSupportBle(): Boolean {
        return bluetoothAdapter != null
    }

    fun isOpenBle():Boolean{
        BluetoothAdapter.ACTION_STATE_CHANGED
        return bluetoothAdapter?.isEnabled == true
    }

    @SuppressLint("MissingPermission")
    fun openBleSetting(context: Context, ) {
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            context.startActivity(enableBtIntent)
        }
    }


    @SuppressLint("MissingPermission")
    fun getBlePairedDevices():Set<BluetoothDevice>?{
        return bluetoothAdapter?.bondedDevices
//        pairedDevices?.forEach { device ->
//            val deviceName = device.name
//            val deviceHardwareAddress = device.address // MAC address
//        }
    }

}