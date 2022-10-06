package com.caldremch.android.logger

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.caldremch.android.log.DebugLogInitializer
import com.caldremch.android.log.debugLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun log(view: View) {
        debugLog { "log printer" }
    }
}