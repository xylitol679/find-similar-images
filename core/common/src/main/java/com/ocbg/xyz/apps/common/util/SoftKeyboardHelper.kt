package com.ocbg.xyz.apps.common.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 软键盘助手
 * User: Tom
 * Date: 2023/10/16 14:54
 */
object SoftKeyboardHelper {

    fun show(context: Context, view: View) {
        try {
            if (view.requestFocus()) {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        } catch (tr: Throwable) {
            // 空
        }
    }

    fun hide(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        } catch (tr: Throwable) {
            // 空
        }
    }

    fun hiddeSoftKeyBoard(context: Activity) {
        if (context == null || context !is Activity || context.currentFocus == null) {
            return
        }
        try {
            val view = context.currentFocus
            val imm =
                view!!.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            view.clearFocus()
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}