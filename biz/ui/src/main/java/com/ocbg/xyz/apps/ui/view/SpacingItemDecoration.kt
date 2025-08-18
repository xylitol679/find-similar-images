package com.ocbg.xyz.apps.ui.view

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil
import kotlin.math.floor

/**
 * 自定义列表项间隔
 * User: Tom
 * Date: 2023/9/14 17:51
 *
 * @property spacing 项间隔，单位像素。在[isIncludeEdge]设置为 true 时起效。
 * @property isIncludeEdge 设置的间隔是否包括边，默认false不包括。
 */
class SpacingItemDecoration(
    private val spacing: Int = 0,
    private val isIncludeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    private var leftSpacing: Int = spacing
    private var rightSpacing: Int = spacing
    private var topSpacing: Int = spacing
    private var bottomSpacing: Int = spacing

    private var leftEdgeSpacing: Int = spacing
    private var rightEdgeSpacing: Int = spacing
    private var topEdgeSpacing: Int = spacing
    private var bottomEdgeSpacing: Int = spacing

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        when (parent.layoutManager) {
            is GridLayoutManager -> setGridLayoutItemOffsets(outRect, view, parent, state)
            is LinearLayoutManager -> setLineLayoutItemOffsets(outRect, view, parent, state)
            else -> {}
        }
    }

    /**
     * 网格布局
     */
    private fun setGridLayoutItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as GridLayoutManager
        val position = parent.getChildAdapterPosition(view)
        val itemCount = layoutManager.itemCount                            // 总项数
        val columnCount = layoutManager.spanCount                          // 总列数
        val rowCount = ceil(itemCount / columnCount.toFloat()).toInt()  // 总行数
        val column = position % columnCount                                // 当前列 [0, columnCount-1]
        val row = floor(position / columnCount.toFloat()).toInt()        // 当前行 [0, rowCount-1]
        Log.i(
            "测试间隔",
            "position: $position, columnCount: $columnCount, rowCount: $rowCount, column: $column, row: $row"
        )

        // 列表布局垂直方向，position 排列顺序是从左到右，从上到下
        // 列表布局水平方向，position 排列顺序是从上到下，从左到右
        // 垂直方向容易理解，水平方向打开显示布局边界，画个图感受下自然会明白。

        if (layoutManager.orientation == RecyclerView.VERTICAL) {
            // 左边。首列，left 设置全值。
            outRect.left = if (column == 0) {
                if (isIncludeEdge) leftEdgeSpacing else 0
            } else {
                leftSpacing / 2
            }

            // 右边。仅一列，right 设置全值。
            outRect.right = if (column == (columnCount - 1)) {
                if (isIncludeEdge) rightEdgeSpacing else 0
            } else {
                rightSpacing / 2
            }

            // 顶边。首行，top 设置全值。
            outRect.top = if (row == 0) {
                if (isIncludeEdge) topEdgeSpacing else 0
            } else {
                topSpacing / 2
            }

            // 底边。末行，bottom 设置全值。
            outRect.bottom = if (row == (rowCount - 1)) {
                if (isIncludeEdge) bottomEdgeSpacing else 0
            } else {
                bottomSpacing / 2
            }
        } else {
            // 左边。首列，top 设置全值。
            outRect.top = if (column == 0) {
                if (isIncludeEdge) topEdgeSpacing else 0
            } else {
                topSpacing / 2
            }

            // 右边。仅一列，bottom 设置全值。
            outRect.bottom = if (column == (columnCount - 1)) {
                if (isIncludeEdge) bottomEdgeSpacing else 0
            } else {
                bottomSpacing / 2
            }

            // 顶边。首行，left 设置全值。
            outRect.left = if (row == 0) {
                if (isIncludeEdge) leftEdgeSpacing else 0
            } else {
                leftSpacing / 2
            }

            // 底边。末行，right 设置全值。
            outRect.right = if (row == (rowCount - 1)) {
                if (isIncludeEdge) rightEdgeSpacing else 0
            } else {
                rightSpacing / 2
            }
        }
    }

    /**
     * 线性布局
     */
    private fun setLineLayoutItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as LinearLayoutManager
        val position = parent.getChildAdapterPosition(view)
        val itemCount = layoutManager.itemCount                            // 总项数

        if (layoutManager.orientation == RecyclerView.VERTICAL) {
            // 左边。首列，left 设置全值。
            outRect.left = if (isIncludeEdge) leftEdgeSpacing else 0

            // 右边。仅一列，right 设置全值。
            outRect.right = if (isIncludeEdge) rightEdgeSpacing else 0

            // 顶边。首行，top 设置全值。
            outRect.top = if (position == 0) {
                if (isIncludeEdge) topEdgeSpacing else 0
            } else {
                topSpacing / 2
            }

            // 底边。末行，bottom 设置全值。
            outRect.bottom = if (position == (itemCount - 1)) {
                if (isIncludeEdge) bottomEdgeSpacing else 0
            } else {
                bottomSpacing / 2
            }
        } else {
            // 左边。首列，left 设置全值。
            outRect.left = if (position == 0) {
                if (isIncludeEdge) leftEdgeSpacing else 0
            } else {
                leftSpacing / 2
            }

            // 右边。仅一列，right 设置全值。
            outRect.right = if (position == (itemCount - 1)) {
                if (isIncludeEdge) rightEdgeSpacing else 0
            } else {
                rightSpacing / 2
            }

            // 顶边。首行，top 设置全值。
            outRect.top = if (isIncludeEdge) topEdgeSpacing else 0

            // 底边。末行，bottom 设置全值。
            outRect.bottom = if (isIncludeEdge) bottomEdgeSpacing else 0
        }
    }

    /**
     * 设置边的间隔值，在[isIncludeEdge]设置为 true 时起效，替代[spacing]值。
     */
    fun setEdgeSpacing(
        left: Int? = null,
        top: Int? = null,
        right: Int? = null,
        bottom: Int? = null
    ) {
        if (left != null && left >= 0) {
            leftEdgeSpacing = left
        }

        if (top != null && top >= 0) {
            topEdgeSpacing = top
        }

        if (right != null && right >= 0) {
            rightEdgeSpacing = right
        }

        if (bottom != null && bottom >= 0) {
            bottomEdgeSpacing = bottom
        }
    }

    /**
     * 设置边的间隔值，在[isIncludeEdge]设置为 true 时起效，替代[spacing]值。
     */
    fun setEdgeSpacing(
        horizontal: Int? = null,
        vertical: Int? = null
    ) {
        if (horizontal != null && horizontal >= 0) {
            leftEdgeSpacing = horizontal
            rightEdgeSpacing = horizontal
        }

        if (vertical != null && vertical >= 0) {
            topEdgeSpacing = vertical
            bottomEdgeSpacing = vertical
        }
    }

    /**
     * 设置间隔值，不包含边。
     */
    fun setSpacing(
        left: Int? = null,
        top: Int? = null,
        right: Int? = null,
        bottom: Int? = null
    ) {
        if (left != null && left >= 0) {
            leftSpacing = left
        }

        if (top != null && top >= 0) {
            topSpacing = top
        }

        if (right != null && right >= 0) {
            rightSpacing = right
        }

        if (bottom != null && bottom >= 0) {
            bottomSpacing = bottom
        }
    }

    /**
     * 设置间隔值，不包含边。
     */
    fun setSpacing(
        horizontal: Int? = null,
        vertical: Int? = null
    ) {
        if (horizontal != null && horizontal >= 0) {
            leftSpacing = horizontal
            rightSpacing = horizontal
        }

        if (vertical != null && vertical >= 0) {
            topSpacing = vertical
            bottomSpacing = vertical
        }
    }

}