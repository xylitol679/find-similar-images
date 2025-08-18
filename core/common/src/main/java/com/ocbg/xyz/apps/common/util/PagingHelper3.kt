package com.ocbg.xyz.apps.common.util

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.drake.statelayout.StateLayout
import com.ocbg.xyz.apps.common.entity.PageInfo
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ocbg.xyz.apps.common.entity.Paging
import com.ocbg.xyz.apps.common.entity.UiState

/**
 * 分页助手（UiState版）
 * User: Tom
 * Date: 2025/3/31 11:39
 */
class PagingHelper3<T : Any, VH : RecyclerView.ViewHolder> @JvmOverloads constructor(
    private val context: Context,
    private var stateLayout: StateLayout? = null,
    private var refreshLayout: SmartRefreshLayout? = null,
    private var adapterHelper: QuickAdapterHelper? = null,
    private var listAdapter: BaseQuickAdapter<T, VH>? = null,
    private var pageInfo: PageInfo? = null
) {

    fun attach(uiState: UiState<Paging<T>?>) {
        when (uiState) {
            is UiState.Failure -> {
                if (pageInfo?.isFirstPage == true) {
                    refreshLayout?.finishRefresh()
                    if (listAdapter?.items.isNullOrEmpty()) {
                        if (stateLayout?.errorLayout != View.NO_ID) {
                            stateLayout?.showError()
                        } else {
                            stateLayout?.showEmpty()
                        }
                    } else {
                        MToast.show(context, uiState.message)
                    }
                } else {
                    MToast.show(context, uiState.message)
                    adapterHelper?.trailingLoadState = LoadState.Error(Throwable(uiState.message))
                }
            }

            UiState.Loading -> {
                stateLayout?.showLoading()
            }

            is UiState.Success -> {
                val paging = uiState.data
                if (paging == null || paging.list.isNullOrEmpty()) {
                    // 无效值
                    if (pageInfo?.isFirstPage == true) {
                        refreshLayout?.finishRefresh()
                        stateLayout?.showEmpty()
                    } else {
                        adapterHelper?.trailingLoadState = LoadState.Error(Throwable("加载异常"))
                    }
                } else {
                    // 有效值
                    if (pageInfo?.isFirstPage == true) {
                        stateLayout?.showContent()
                        refreshLayout?.finishRefresh()
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