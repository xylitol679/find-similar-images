package com.ocbg.xyz.apps.common.util

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.ocbg.xyz.apps.common.entity.PageInfo
import com.ocbg.xyz.apps.common.entity.Paging

/**
 * 分页助手
 * User: Tom
 * Date: 2024/4/30 14:25
 */
class PagingHelper2<T : Any, VH : RecyclerView.ViewHolder, EVH : RecyclerView.ViewHolder> @JvmOverloads constructor(
    private val context: Context,
    private var adapterHelper: QuickAdapterHelper? = null,
    private var listAdapter: BaseQuickAdapter<T, VH>? = null,
    private var emptyAdapter: BaseQuickAdapter<Any, EVH>? = null,
    private var pageInfo: PageInfo? = null
) {

    fun attach(result: Result<Paging<T>?>) {
        if (result.isSuccess) {
            val paging = result.getOrNull()
            if (paging == null || paging.list.isNullOrEmpty()) {
                // 无效值
                if (pageInfo?.isFirstPage == true) {
                    // 显示空视图并且设置无数据了
                    if (adapterHelper?.afterAdapterList?.isEmpty() == true) {
                        emptyAdapter?.let { adapterHelper?.addAfterAdapter(it) }
                    }
                    adapterHelper?.trailingLoadState = LoadState.NotLoading(true)
                } else {
                    adapterHelper?.clearAfterAdapters()
                    adapterHelper?.trailingLoadState =
                        LoadState.Error(result.exceptionOrNull() ?: Throwable("加载异常"))
                }
            } else {
                adapterHelper?.clearAfterAdapters()
                // 有效值
                if (pageInfo?.isFirstPage == true) {
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
                if (listAdapter?.items.isNullOrEmpty()) {
                    // 显示空视图并且设置无数据了
                    if (adapterHelper?.afterAdapterList?.isEmpty() == true) {
                        emptyAdapter?.let { adapterHelper?.addAfterAdapter(it) }
                    }
                    adapterHelper?.trailingLoadState = LoadState.NotLoading(true)
                } else {
                    adapterHelper?.clearAfterAdapters()
                    result.exceptionOrNull()?.message?.let { MToast.show(context, it) }
                }
            } else {
                adapterHelper?.clearAfterAdapters()
                result.exceptionOrNull()?.message?.let { MToast.show(context, it) }
                adapterHelper?.trailingLoadState =
                    LoadState.Error(result.exceptionOrNull() ?: Throwable("加载异常"))
            }
        }
    }

    fun detach() {
        adapterHelper = null
        listAdapter = null
    }
}