package xyz.xyzmu.sitest.apps.pc.duplicate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.chad.library.adapter4.BaseQuickAdapter
import xyz.xyzmu.sitest.apps.pc.duplicate.R
import xyz.xyzmu.sitest.apps.pc.duplicate.databinding.DiListItemGridBinding
import xyz.xyzmu.sitest.apps.pc.duplicate.databinding.DiListItemHeaderBinding
import xyz.xyzmu.sitest.apps.pc.duplicate.entity.ItemGroup
import xyz.xyzmu.sitest.apps.pc.duplicate.entity.SelectableItem
import java.io.File


/**
 * 重复图片-扫描结果 Adapter
 * User: Tom
 * Date: 2025/8/13 19:34
 */
internal class ScanResultListAdapter : BaseMultiItemAdapter<ItemGroup>() {

    private var groupStr: String? = null

    /** 列表项选中监听器 */
    private var itemSelectListener: OnItemSelectListener? = null

    companion object {
        private const val HEADER = 0
        private const val GRID = 1
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        groupStr = context.getString(R.string.di_group)
    }

    inner class HeaderVH(
        private val binding: DiListItemHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, data: ItemGroup) {
            binding.tvDate.text = "$groupStr${data.index + 1}"
            binding.ivSelectGroup.isSelected = data.isSelected
            binding.tvDate.setOnClickListener {
                selectItemGroup(position)
            }
        }
    }

    inner class GridVH(
        private val binding: DiListItemGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, data: ItemGroup) {
            (binding.rvFiles.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            binding.rvFiles.setHasFixedSize(true)
            binding.rvFiles.adapter = ImageAdapter(data.items).apply {
                setOnItemClickListener { gridAdapter, _, gridPosition ->
                    selectItem(position, gridAdapter, gridPosition)
                }
            }
        }
    }

    init {
        addItemType(
            HEADER,
            object : OnMultiItemAdapterListener<ItemGroup, HeaderVH> {
                override fun onBind(
                    holder: HeaderVH,
                    position: Int,
                    item: ItemGroup?
                ) {
                    item?.let { holder.bind(position, it) }
                }

                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): HeaderVH {
                    val binding = DiListItemHeaderBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                    return HeaderVH(binding)
                }
            })
            .addItemType(
                GRID,
                object : OnMultiItemAdapterListener<ItemGroup, GridVH> {
                    override fun onBind(
                        holder: GridVH,
                        position: Int,
                        item: ItemGroup?
                    ) {
                        item?.let { holder.bind(position, it) }
                    }

                    override fun onCreate(
                        context: Context,
                        parent: ViewGroup,
                        viewType: Int
                    ): GridVH {
                        val binding = DiListItemGridBinding.inflate(
                            LayoutInflater.from(context), parent, false
                        )
                        return GridVH(binding)
                    }
                })
            .onItemViewType { position, list ->
                if (list.getOrNull(position)?.items?.isEmpty() == true) HEADER else GRID
            }
    }

    /**
     * 全选
     * @param isSelectAll true表示全选，false表示取消全选
     */
    fun selectAllItems(isSelectAll: Boolean) {
        items.forEachIndexed { index, itemGroup ->
            if (index % 2 == 0) {
                itemGroup.isSelected = isSelectAll
            } else {
                itemGroup.items.forEach { item ->
                    item.isSelected = isSelectAll
                }
            }
        }
        notifyDataSetChanged()

        itemSelectListener?.onChanged(getSelectedItems(), isSelectAll)
    }

    /**
     * 组选
     * @param position 列表 Position
     */
    private fun selectItemGroup(position: Int) {
        val groupHeader = getItem(position) ?: return
        if (groupHeader.isSelected) {
            // 取消选中组内所有项
            val groupItem = getItem(position + 1) ?: return
            groupItem.items.forEach {
                it.isSelected = false
            }

            groupHeader.isSelected = false
            notifyItemRangeChanged(position, 2)
        } else {
            // 选中组内所有项
            val groupItem = getItem(position + 1) ?: return
            groupItem.items.forEach {
                it.isSelected = true
            }

            groupHeader.isSelected = true
            notifyItemRangeChanged(position, 2)
        }

        // 检查全选状态
        val isAllSelected = checkAllSelected()

        itemSelectListener?.onChanged(getSelectedItems(), isAllSelected)
    }

    /**
     * 项选
     * @param position 列表 Position
     * @param gridPosition 组内列表 Position
     */
    private fun selectItem(
        position: Int,
        gridAdapter: BaseQuickAdapter<SelectableItem, *>,
        gridPosition: Int
    ) {
        val item = gridAdapter.getItem(gridPosition) ?: return
        item.isSelected = !item.isSelected
        gridAdapter.notifyItemChanged(gridPosition)

        // 检查组内全选状态
        val isItemGroupSelected = checkItemGroupSelected(position)
        val groupPosition = position - 1
        val itemGroup = getItem(groupPosition) ?: return
        if (itemGroup.isSelected != isItemGroupSelected) {
            itemGroup.isSelected = isItemGroupSelected
            notifyItemChanged(groupPosition)
        }

        // 检查全选状态
        val isAllSelected = checkAllSelected()

        itemSelectListener?.onChanged(getSelectedItems(), isAllSelected)
    }

    private fun checkItemGroupSelected(position: Int): Boolean {
        val itemGroup = getItem(position) ?: return false
        itemGroup.items.forEach {
            if (!it.isSelected) {
                return false
            }
        }
        return true
    }

    private fun checkAllSelected(): Boolean {
        items.forEachIndexed { index, itemGroup ->
            if (index % 2 == 0) {
                if (!itemGroup.isSelected) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 获取已选中项
     */
    fun getSelectedItems() = buildList<File> {
        items.forEachIndexed { index, itemGroup ->
            if (index % 2 != 0) {
                itemGroup.items.forEach {
                    if (it.isSelected) {
                        add(it.file)
                    }
                }
            }
        }
    }

    /**
     * 获取未选中项
     */
    fun getUnselectedItems() = buildList<File> {
        items.forEachIndexed { index, itemGroup ->
            if (index % 2 != 0) {
                itemGroup.items.forEach {
                    if (!it.isSelected) {
                        add(it.file)
                    }
                }
            }
        }
    }

    /**
     * 项选变化监听
     */
    fun interface OnItemSelectListener {
        fun onChanged(positions: List<File>, isSelectAll: Boolean)
    }

    /**
     * 设置项选变化监听器
     */
    fun setOnItemSelectListener(listener: OnItemSelectListener) = apply {
        itemSelectListener = listener
    }

}