package com.caldremch.android.log.impl

/**
 * Created by Leon on 2022/8/30
 */
object Platform {
    val isAndroid: Boolean
        get() = "Dalvik" == System.getProperty("java.vm.name")
//
//    private fun findPlatform(): Platform = if (isAndroid) {
//        findAndroidPlatform()
//    } else {
//        findJvmPlatform()
//    }
}