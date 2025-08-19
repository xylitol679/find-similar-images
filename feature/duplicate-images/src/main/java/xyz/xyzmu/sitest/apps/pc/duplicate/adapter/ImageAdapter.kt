package xyz.xyzmu.sitest.apps.pc.duplicate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.ocbg.xyz.apps.common.util.MFileUtil
import xyz.xyzmu.sitest.apps.pc.duplicate.databinding.DiListItemImageBinding
import xyz.xyzmu.sitest.apps.pc.duplicate.entity.SelectableItem

/**
 * 扫描结果 Adapter
 * User: Tom
 * Date: 2025/8/7 10:20
 */
internal class ImageAdapter(list: List<SelectableItem>) :
    BaseQuickAdapter<SelectableItem, ImageAdapter.CustomVH>(list) {

    private val selectedPosition = mutableSetOf<Int>()

    class CustomVH(
        parent: ViewGroup,
        val binding: DiListItemImageBinding = DiListItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(
        holder: CustomVH,
        position: Int,
        item: SelectableItem?
    ) {
        if (item == null) {
            return
        }

        Glide.with(holder.binding.ivFileIcon)
            .load(item.file.absolutePath)
            .into(holder.binding.ivFileIcon)

        holder.binding.tvFileSize.text = MFileUtil.formatFileSize(item.file.length())

        holder.binding.ivSelected.isSelected = item.isSelected
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): CustomVH {
        return CustomVH(parent)
    }

}