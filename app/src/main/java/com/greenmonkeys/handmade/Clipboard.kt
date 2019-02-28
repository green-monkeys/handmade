package com.greenmonkeys.handmade

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object Clipboard {
    fun copyValueToClipboard(itemName: String, value: String, context: Context) {
        val clipData = ClipData.newPlainText(itemName, value)
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.primaryClip = clipData
    }
}