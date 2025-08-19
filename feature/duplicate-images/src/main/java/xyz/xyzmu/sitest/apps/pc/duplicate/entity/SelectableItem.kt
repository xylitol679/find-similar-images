package xyz.xyzmu.sitest.apps.pc.duplicate.entity

import java.io.File

/**
 * 可选项
 * User: Tom
 * Date: 2025/8/15 9:37
 *
 * @property file 文件
 * @property isSelected 选中状态，true表示选中
 */
data class SelectableItem(
    val file: File,
    var isSelected: Boolean
)
