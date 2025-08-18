package com.ocbg.xyz.apps.common.util

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.ocbg.xyz.apps.common.entity.PageInfo
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ocbg.xyz.apps.common.entity.Paging
import com.ocbg.xyz.apps.common.entity.UiState

/**
 * 分页助手（UiState版）
 * User: Tom
 * Date: 2025/4/21 19:14
 */
class PagingHelper4<T : Any, VH : RecyclerView.ViewHolder, EVH : RecyclerView.ViewHolder, ERVH : RecyclerView.ViewHolder, LVH : RecyclerView.ViewHolder> @JvmOverloads constructor(
    private val context: Context,
    private var refreshLayout: SmartRefreshLayout? = null,
    private var adapterHelper: QuickAdapterHelper? = null,
    private var listAdapter: BaseQuickAdapter<T, VH>? = null,
    private var emptyAdapter: BaseQuickAdapter<Any, EVH>? = null,
    private var errorAdapter: BaseQuickAdapter<Any, ERVH>? = null,
    private var loadingAdapter: BaseQuickAdapter<Any, LVH>? = null,
    private var pageInfo: PageInfo? = null
) {

    fun attach(uiState: UiState<Paging<T>?>) {
        when (uiState) {
            is UiState.Failure -> {
                if (pageInfo?.isFirstPage == true) {
                    refreshLayout?.finishRefresh()
                    if (listAdapter?.items.isNullOrEmpty()) {
                        adapterHelper?.clearAfterAdapters()
                        errorAdapter?.let { adapterHelper?.addAfterAdapter(it) }
                    } else {
                        MToast.show(context, uiState.message)
                    }
                } else {
                    MToast.show(context, uiState.message)
                    adapterHelper?.trailingLoadState = LoadState.Error(Throwable(uiState.message))
                }
            }

            UiState.Loading -> {
                adapterHelper?.clearAfterAdapters()
                loadingAdapter?.let { adapterHelper?.addAfterAdapter(it) }
            }

            is UiState.Success -> {
                adapterHelper?.clearAfterAdapters()
                val paging = uiState.data
                if (paging == null || paging.list.isNullOrEmpty()) {
                    // 无效值
                    if (pageInfo?.isFirstPage == true) {
                        refreshLayout?.finishRefresh()
                        listAdapter?.submitList(mutableListOf())
                        emptyAdapter?.let { adapterHelper?.addAfterAdapter(it) }
                    } else {
                        adapterHelper?.trailingLoadState = LoadState.Error(Throwable("加载异常"))
                    }
                } else {
                    // 有效值
                    if (pageInfo?.isFirstPage == true) {
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
        refreshLayout = null
        adapterHelper = null
        listAdapter = null
        emptyAdapter = null
        errorAdapter = null
        loadingAdapter = null
    }
}