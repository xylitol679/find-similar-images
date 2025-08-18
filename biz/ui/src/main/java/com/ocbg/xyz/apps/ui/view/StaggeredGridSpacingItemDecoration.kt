package com.ocbg.xyz.apps.ui.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @Description:
 * @Author: Donny
 * @CreateDate: 2025/4/1
 * @Version: 1.0
 */
class StaggeredGridSpacingItemDecoration(
    private val spanCount: Int, // 列数（2 列）
    private val spacing: Int // 12dp
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val layoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val column = layoutParams.spanIndex // 获取 item 所在列（0 或 1）

        // **列间距**
        outRect.left = if (column == 0) 0 else spacing / 2 // 第一列左边 0，第二列左边 6dp
        outRect.right = if (column == 0) spacing / 2 else 0 // 第一列右边 6dp，第二列右边 0

        // **行间距**
        outRect.top = spacing // 确保行间距是 12dp
    }
}