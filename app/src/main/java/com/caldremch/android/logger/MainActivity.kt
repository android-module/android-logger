package com.caldremch.android.logger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.caldremch.android.log.DebugLogInitializer
import com.caldremch.android.log.debugLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DebugLogInitializer.init(BuildConfig.DEBUG)

        debugLog { "log printer" }
    }
}