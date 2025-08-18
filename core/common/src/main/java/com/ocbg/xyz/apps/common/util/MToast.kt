package com.ocbg.xyz.apps.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 简易Toast
 * User: Tom
 * Date: 2023/9/28 14:19
 */
object MToast {

    private var sToast: Toast? = null

    @SuppressLint("ShowToast")
    fun show(context: Context, text: String?) {
        if (text == null) {
            return
        }
        if (sToast == null) {
            sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        } else {
            sToast?.setText(text)
        }
        sToast?.show()
    }

    @SuppressLint("ShowToast")
    fun show(context: Context, @StringRes resId: Int) {
        if (sToast == null) {
            sToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT)
        } else {
            sToast?.setText(resId)
        }
        sToast?.show()
    }

    @SuppressLint("ShowToast")
    fun showLong(context: Context, text: String?) {
        if (text == null) {
            return
        }
        if (sToast == null) {
            sToast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        } else {
            sToast?.setText(text)
        }
        sToast?.show()
    }

    @SuppressLint("ShowToast")
    fun showLong(context: Context, @StringRes resId: Int) {
        if (sToast == null) {
            sToast = Toast.makeText(context, resId, Toast.LENGTH_LONG)
        } else {
            sToast?.setText(resId)
        }
        sToast?.show()
    }

    fun cancel() {
        sToast?.cancel()
    }

}