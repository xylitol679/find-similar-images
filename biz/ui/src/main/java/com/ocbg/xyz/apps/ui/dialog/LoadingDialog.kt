package com.ocbg.xyz.apps.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.ocbg.xyz.apps.ui.R

/**
 * 加载对话框
 * User: Tom
 * Date: 2023/10/20 11:33
 */
class LoadingDialog(
    context: Context,
    private val cancelable: Boolean = false,
    private val message: String? = null,
    private val transparent: Boolean = true
) : Dialog(context, R.style.ui_LoadingDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_dialog_loading)
        setCancelable(cancelable)
        setCanceledOnTouchOutside(cancelable)
        if (transparent) window?.setDimAmount(0f)
        setMessage(message)
    }

    fun setMyCancelable(cancelable: Boolean) {
        setCancelable(cancelable)
        setCanceledOnTouchOutside(cancelable)
    }

    fun setMessage(message: String? = null) {
        val textView = findViewById<TextView>(R.id.tvMessage)
        if (message.isNullOrEmpty()) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = message
        }
    }

}