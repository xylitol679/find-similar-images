package com.ocbg.xyz.apps.common.util

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.drake.statelayout.StateLayout
import com.ocbg.xyz.apps.common.entity.PageInfo
import com.ocbg.xyz.apps.common.entity.Paging
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 分页助手
 * User: Tom
 * Date: 2024/4/30 14:25
 */
class PagingHelper<T : Any, VH : RecyclerView.ViewHolder> @JvmOverloads constructor(
    private val context: Context,
    private var stateLayout: StateLayout? = null,
    private var refreshLayout: SmartRefreshLayout? = null,
    private var adapterHelper: QuickAdapterHelper? = null,
    private var listAdapter: BaseQuickAdapter<T, VH>? = null,
    private var pageInfo: PageInfo? = null
) {

    fun attach(result: Result<Paging<T>?>) {
        if (result.isSuccess) {
            val paging = result.getOrNull()
            if (paging == null || paging.list.isNullOrEmpty()) {
                // 无效值
                if (pageInfo?.isFirstPage == true) {
                    refreshLayout?.finishRefresh()
                    stateLayout?.showEmpty()
                } else {
                    adapterHelper?.trailingLoadState =
                        LoadState.Error(result.exceptionOrNull() ?: Throwable("加载异常"))
                }
            } else {
                // 有效值
                if (pageInfo?.isFirstPage == true) {
                    refreshLayout?.finishRefresh()
                    stateLayout?.showContent()
                    listAdapter?.submitList(paging.list)
                } else {
                    listAdapter?.addAll(paging.list)
                }

                if ((listAdapter?.items?.size ?: 0) < paging.totalSize) {
                    adapterHelper?.trailingLoadState = LoadState.NotLoading(false)
                    pageInfo?.nextPage()
                } else {
                    adapterHelper?.trailingLoadState = LoadState.NotLoading(true)
                }
            }
        } else {
            if (pageInfo?.isFirstPage == true) {
                refreshLayout?.finishRefresh()
                if (listAdapter?.items.isNullOrEmpty()) {
                    if (stateLayout?.errorLayout != View.NO_ID) {
                        stateLayout?.showError()
                    } else {
                        stateLayout?.showEmpty()
                    }
                } else {
                    result.exceptionOrNull()?.message?.let { MToast.show(context, it) }
                }
            } else {
                result.exceptionOrNull()?.message?.let { MToast.show(context, it) }
                adapterHelper?.trailingLoadState =
                    LoadState.Error(result.exceptionOrNull() ?: Throwable("加载异常"))
            }
        }
    }

    fun detach() {
        stateLayout = null
        refreshLayout = null
        adapterHelper = null
        listAdapter = null
    }
}