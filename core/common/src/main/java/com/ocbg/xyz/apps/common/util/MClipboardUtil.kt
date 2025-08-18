package com.ocbg.xyz.apps.common.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * 剪切板的工具类
 * User: Donny
 * Date: 2025/01/16
 *
 */
object MClipboardUtil {

    /**
     * 获取剪切板文本的函数
     *
     * @param context
     * @return
     */
    fun getClipboardText(context: Context): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip

        // 检查剪切板是否有内容，并且该内容是否为文本
        if (clip != null && clip.itemCount > 0) {
            val item = clip.getItemAt(0)
            if (item != null && item.text != null) {
                return item.text.toString()
            }
        }
        return null
    }

    /**
     * 复制文本到粘贴板
     */
    fun copyText(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TEXT", text)
        clipboard.setPrimaryClip(clip)
    }

}