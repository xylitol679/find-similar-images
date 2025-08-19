package xyz.xyzmu.sitest.apps.pc.duplicate.entity

/**
 * 项组
 * User: Tom
 * Date: 2025/8/15 9:38
 *
 * @property index 组号
 * @property items 组内所有项
 * @property isSelected 组选中状态，true表示选中
 */
data class ItemGroup(
    val index: Int,
    val items: List<SelectableItem>,
    var isSelected: Boolean
)
