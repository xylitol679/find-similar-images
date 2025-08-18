package com.ocbg.xyz.apps.ui.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.scwang.smart.refresh.layout.util.SmartUtil
import androidx.core.graphics.toColorInt

/**
 * 自定义下拉刷新，可参考https://github.com/scwang90/SmartRefreshLayout
 * User: Tom
 * Date: 2023/9/28 16:27
 */
class CustomRefreshHeader : LinearLayout, RefreshHeader {

    private val loadingViewSize = 30f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        gravity = Gravity.CENTER
        val loadingView= CircularProgressIndicator(context)
        loadingView.setIndicatorColor("#73F9FE".toColorInt())
        loadingView.trackColor = "#4D333333".toColorInt()
        loadingView.trackThickness = SmartUtil.dp2px(2f)
        loadingView.indicatorSize = SmartUtil.dp2px(loadingViewSize)
        loadingView.isIndeterminate = true
        addView(loadingView, SmartUtil.dp2px(loadingViewSize), SmartUtil.dp2px(loadingViewSize))
        minimumHeight = SmartUtil.dp2px(60f)
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when (newState) {
            RefreshState.PullDownToRefresh -> {
            }

            RefreshState.Refreshing -> {
            }

            RefreshState.RefreshFinish -> {
            }

            else -> {}
        }
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return 500
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun autoOpen(duration: Int, dragRate: Float, animationOnly: Boolean): Boolean {
        return false
    }

}