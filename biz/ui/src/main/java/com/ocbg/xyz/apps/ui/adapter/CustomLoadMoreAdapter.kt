package com.ocbg.xyz.apps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.ocbg.xyz.apps.ui.databinding.UiLayoutListLoadMoreBinding

/**
 * 自定义加载更多适配器
 * User: Tom
 * Date: 2023/9/13 17:58
 */
class CustomLoadMoreAdapter : TrailingLoadStateAdapter<CustomLoadMoreAdapter.CustomLoadMoreVH>() {

    class CustomLoadMoreVH(val binding: UiLayoutListLoadMoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: CustomLoadMoreVH, loadState: LoadState) {
        when (loadState) {
            is LoadState.NotLoading -> {
                if (loadState.endOfPaginationReached) {
                    holder.binding.loading.isVisible = false
                    holder.binding.complete.isVisible = false
                    holder.binding.fail.isVisible = false
                    holder.binding.end.isVisible = true
                } else {
                    holder.binding.loading.isVisible = false
                    holder.binding.end.isVisible = false
                    holder.binding.fail.isVisible = false
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
                holder.binding.end.isVisible = false
                holder.binding.fail.isVisible = false
                holder.binding.loading.isVisible = true
            }

            is LoadState.None -> {
                holder.binding.loading.isVisible = false
                holder.binding.complete.isVisible = false
                holder.binding.end.isVisible = false
                holder.binding.fail.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CustomLoadMoreVH {
        return CustomLoadMoreVH(
            UiLayoutListLoadMoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            binding.tvRetry.setOnClickListener {
                invokeFailRetry()
            }

            binding.tvLoadMore.setOnClickListener {
                invokeLoadMore()
            }
        }
    }

    override fun onViewAttachedToWindow(holder: CustomLoadMoreVH) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams) {
            lp.isFullSpan = true
        }
    }

}