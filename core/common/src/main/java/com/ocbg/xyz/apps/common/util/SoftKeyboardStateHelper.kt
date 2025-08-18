package com.ocbg.xyz.apps.common.util

import android.R.id.content
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener

/**
 * @Description: java类作用描述
 * @Author: ang
 * @CreateDate: 2021/10/19 19:49
 * @Version: 1.1
 */
class SoftKeyboardStateHelper(
    private val decorView: View,
    /** 状态栏高度 */
    private val statusBarHeight: Int,
    /** 是否存在导航栏 */
    private var isNavigationBar: Boolean,
    /** 导航栏高度 */
    private val navigationBarHeight: Int
) : OnGlobalLayoutListener {
    private var isSoftKeyboardOpened = false
    private var softKeyboardHeight = 0
    private var softKeyboardStateListener: SoftKeyboardStateListener? = null

    fun getSoftKeyHeight() = softKeyboardHeight

    fun registerSoftKeyboardStateListener(softKeyboardStateListener: SoftKeyboardStateListener?) {
        try {
            decorView.findViewById<View>(content).viewTreeObserver
                .addOnGlobalLayoutListener(this)
            this.softKeyboardStateListener = softKeyboardStateListener
        } catch (tr: Throwable) {
            // 空
        }
    }

    fun unregisterSoftKeyboardStateListener() {
        try {
            decorView.findViewById<View>(content).viewTreeObserver
                .removeOnGlobalLayoutListener(this)
        } catch (tr: Throwable) {
            // 空
        } finally {
            softKeyboardStateListener = null
        }
    }

    override fun onGlobalLayout() {
        if (isKeyboardShown(decorView)) {
            //键盘弹出
            if (!isSoftKeyboardOpened) {
                val r = Rect()
                decorView.getWindowVisibleDisplayFrame(r)
                val mutableHeight = r.bottom - r.top
                val screenHeight: Int = decorView.height
                softKeyboardHeight =
                    screenHeight - mutableHeight - statusBarHeight - if (isNavigationBar) navigationBarHeight else 0

                Log.i(
                    "软键盘监听器助手",
                    "展开, screenHeight=${screenHeight}, mutableHeight = ${mutableHeight}, softKeyHeight=${softKeyboardHeight}, statusBarHeight=${statusBarHeight}, isNavigationBar=${isNavigationBar}, navigationBarHeight=${navigationBarHeight}"
                )
                isSoftKeyboardOpened = true
                softKeyboardStateListener?.onSoftKeyboardOpened(softKeyboardHeight)
            }
        } else {
            //键盘收起
            if (isSoftKeyboardOpened) {
                Log.i("软键盘监听器助手", "关闭")
                isSoftKeyboardOpened = false
                softKeyboardStateListener?.onSoftKeyboardClosed()
            }
        }
    }

    private fun isKeyboardShown(rootView: View): Boolean {
        val softKeyboardHeight = 100
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val dm = rootView.resources.displayMetrics
        val heightDiff = rootView.bottom - r.bottom
        return heightDiff > softKeyboardHeight * dm.density
    }

    interface SoftKeyboardStateListener {
        fun onSoftKeyboardOpened(softKeyboardHeight: Int)
        fun onSoftKeyboardClosed()
    }
}