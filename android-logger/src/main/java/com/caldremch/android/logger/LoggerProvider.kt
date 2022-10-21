package com.caldremch.android.logger

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.caldremch.android.log.DebugLogInitializer

/**
 * Created by Leon on 2022/10/21.
 */
class LoggerProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        DebugLogInitializer.initWithDetect(true)
        LoggerLifeCycleManager.lifeCycleRegister()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0
}