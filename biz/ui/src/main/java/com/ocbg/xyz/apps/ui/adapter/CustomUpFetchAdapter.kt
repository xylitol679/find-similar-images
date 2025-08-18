package com.ocbg.xyz.apps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.leading.LeadingLoadStateAdapter
import com.ocbg.xyz.apps.ui.databinding.UiLayoutListUpFetchBinding

/**
 * 自定义向上获取 Adapter
 * User: Tom
 * Date: 2025/3/18 16:18
 */
class CustomUpFetchAdapter : LeadingLoadStateAdapter<CustomUpFetchAdapter.CustomUpFetchVH>() {

    class CustomUpFetchVH(val binding: UiLayoutListUpFetchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: CustomUpFetchVH, loadState: LoadState) {
        when (loadState) {
            is LoadState.NotLoading -> {
                if (loadState.endOfPaginationReached) {
                    holder.binding.loading.isVisible = false
                    holder.binding.complete.isVisible = false
                    holder.binding.fail.isVisible = false
                    holder.binding.end.isVisible = true
                } else {
                    holder.binding.loading.isVisible = false
                    holder.binding.fail.isVisible = false
                    holder.binding.end.isVisible = false
                    holder.binding.complete.isVisible = true
                }
            }

            is LoadState.Error -> {
                holder.binding.loading.isVisible = false
                holder.binding.complete.isVisible = false
                holder.binding.end.isVisible = false
                holder.binding.fail.isVisible = true
            }

            is LoadState.Loading -> {
                holder.binding.complete.isVisible = false
                holder.binding.fail.isVisible = false
                holder.binding.end.isVisible = false
                holder.binding.loading.isVisible = true
            }

            is LoadState.None -> {
                holder.binding.loading.isVisible = false
                holder.binding.complete.isVisible = false
                holder.binding.fail.isVisible = false
                holder.binding.end.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CustomUpFetchVH {
        return CustomUpFetchVH(
            UiLayoutListUpFetchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            binding.tvLoadMore.setOnClickListener {
                invokeLoad()
            }
        }
    }

    override fun onViewAttachedToWindow(holder: CustomUpFetchVH) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams) {
            lp.isFullSpan = true
        }
    }

}