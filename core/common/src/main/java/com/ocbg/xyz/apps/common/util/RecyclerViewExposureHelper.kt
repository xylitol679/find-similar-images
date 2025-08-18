package com.ocbg.xyz.apps.common.util

import android.os.CountDownTimer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * RecyclerView 曝光助手
 * User: Tom
 * Date: 2023/11/21 19:18
 */
class RecyclerViewExposureHelper(
    private val recyclerView: RecyclerView,
    private val exposureThreshold: Long,
    private val listener: (positions: List<Int>) -> Unit
) {
    private val scrollItemMap: HashMap<Int, MutableSet<Long>> = hashMapOf()
    private val idleItemMap: HashMap<Int, MutableSet<Long>> = hashMapOf()
    private var scrollListener: RecyclerView.OnScrollListener? = null
    private var idleItemTimer: CountDownTimer? = null
    private var isFirst = true

    fun attach() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        com.ocbg.xyz.apps.common.util.MLog.d(
                            com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG_STATE,
                            "=> 拖"
                        )
                        idleItemTimer?.cancel()
                        saveCompletelyVisibleItemsTime(scrollItemMap, recyclerView.layoutManager)
                    }

                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        com.ocbg.xyz.apps.common.util.MLog.d(
                            com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG_STATE,
                            "=> 惯"
                        )
                        saveCompletelyVisibleItemsTime(scrollItemMap, recyclerView.layoutManager)
                    }

                    RecyclerView.SCROLL_STATE_IDLE -> {
                        com.ocbg.xyz.apps.common.util.MLog.d(
                            com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG_STATE,
                            "=> 停"
                        )
                        saveCompletelyVisibleItemsTime(scrollItemMap, recyclerView.layoutManager)
                        listener.invoke(calculateExposureItems(scrollItemMap, exposureThreshold))

                        // 停下来后，接下来没有操作，倒计时满足曝光时间后记入
                        startCalculateExposureIdleItems()
                    }

                    else -> {}
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                com.ocbg.xyz.apps.common.util.MLog.d(
                    com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG_STATE,
                    "滚动：$dx, $dy"
                )
                if (isFirst) {
                    isFirst = false
                    // 初始化的完成的时候来到这里
                    startCalculateExposureIdleItems()
                } else {
                    saveCompletelyVisibleItemsTime(scrollItemMap, recyclerView.layoutManager)
                }
            }
        }
        scrollListener?.let { recyclerView.addOnScrollListener(it) }

        idleItemTimer = object : CountDownTimer(exposureThreshold, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                listener.invoke(calculateExposureIdleItems(idleItemMap))
            }
        }
    }

    fun detach() {
        idleItemTimer?.cancel()
        scrollListener?.let { recyclerView.removeOnScrollListener(it) }
        scrollListener = null
    }

    /**
     * 计算完全显示的项位置范围
     * @param layoutManager
     * @return 项位置范围
     */
    private fun calculateCompletelyVisibleItemPositionRange(layoutManager: RecyclerView.LayoutManager?): IntRange? {
        when (layoutManager) {
            is GridLayoutManager -> {
                val start = layoutManager.findFirstCompletelyVisibleItemPosition()
                val end = layoutManager.findLastCompletelyVisibleItemPosition()
                return IntRange(start, end)
            }

            is LinearLayoutManager -> {
                val start = layoutManager.findFirstCompletelyVisibleItemPosition()
                val end = layoutManager.findLastCompletelyVisibleItemPosition()
                return IntRange(start, end)
            }

            is StaggeredGridLayoutManager -> {
                val startInto = IntArray(layoutManager.spanCount)
                val endInto = IntArray(layoutManager.spanCount)
                layoutManager.findFirstCompletelyVisibleItemPositions(startInto)
                layoutManager.findLastCompletelyVisibleItemPositions(endInto)
                return IntRange(startInto.min(), endInto.max())
            }

            else -> {
                return null
            }
        }
    }

    /**
     * 保存完全显示项的时间
     */
    private fun saveCompletelyVisibleItemsTime(
        map: HashMap<Int, MutableSet<Long>>,
        layoutManager: RecyclerView.LayoutManager?
    ) {
        if (layoutManager == null) return
        val time = System.currentTimeMillis()
        val range = calculateCompletelyVisibleItemPositionRange(layoutManager) ?: return
        com.ocbg.xyz.apps.common.util.MLog.d(
            com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG,
            "time:$time"
        )
        com.ocbg.xyz.apps.common.util.MLog.d(
            com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG,
            "range: [${range.first}, ${range.last}]"
        )
        saveVisibleItemsTime(map, range, time)
    }

    /**
     * 保存显示项的时间
     * @param range 项位置范围
     * @param time 曝光时间
     */
    private fun saveVisibleItemsTime(
        map: HashMap<Int, MutableSet<Long>>,
        range: IntRange,
        time: Long
    ) {
        range.forEach { key ->
            val value = map[key]
            if (value == null) {
                map[key] = mutableSetOf(time)
            } else {
                value.add(time)
            }
        }
    }

    /**
     * 计算符合曝光条件的项位置
     * @param threshold 满足曝光的时间阈值，单位毫秒
     */
    private fun calculateExposureItems(
        map: HashMap<Int, MutableSet<Long>>,
        threshold: Long
    ): List<Int> {
        com.ocbg.xyz.apps.common.util.MLog.d(
            com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG_RESULT,
            "map: $map"
        )
        val list = map.mapValues { item ->
            item.value.max() - item.value.min()
        }.apply {
            com.ocbg.xyz.apps.common.util.MLog.d(
                com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG_RESULT,
                "map mv: $this"
            )
        }.mapNotNull {
            if (it.value >= threshold) it.key else null
        }
        com.ocbg.xyz.apps.common.util.MLog.d(
            com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG_RESULT,
            "list: $list"
        )
        map.clear()
        return list
    }

    /**
     * 计算符合曝光条件的项位置
     */
    private fun calculateExposureIdleItems(map: HashMap<Int, MutableSet<Long>>): List<Int> {
        com.ocbg.xyz.apps.common.util.MLog.d(
            com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG_RESULT,
            "map: $map"
        )
        val list = map.mapNotNull {
            it.key
        }
        com.ocbg.xyz.apps.common.util.MLog.d(
            com.ocbg.xyz.apps.common.util.RecyclerViewExposureHelper.Companion.TAG_RESULT,
            "list: $list"
        )
        map.clear()
        return list
    }

    private fun startCalculateExposureIdleItems() {
        idleItemMap.clear()
        saveCompletelyVisibleItemsTime(idleItemMap, recyclerView.layoutManager)
        idleItemTimer?.start()
    }

    companion object {
        private const val TAG = "曝光"
        private const val TAG_STATE = "曝光 状态"
        private const val TAG_RESULT = "曝光 结果"
    }

}