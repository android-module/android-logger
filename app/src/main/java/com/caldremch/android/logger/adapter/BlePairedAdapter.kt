package com.caldremch.android.logger.adapter

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.ViewGroup
import com.caldremch.android.logger.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by Leon on 2022/10/20.
 */
class BlePairedAdapter() : BaseQuickAdapter<BluetoothDevice, BaseViewHolder>(
    R.layout.item
) {

    override fun convert(holder: BaseViewHolder, item: BluetoothDevice) {
    }
}