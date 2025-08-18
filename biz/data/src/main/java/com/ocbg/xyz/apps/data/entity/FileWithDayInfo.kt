package com.ocbg.xyz.apps.data.entity

import isApkFile
import isAudioFile
import java.io.File


sealed class FileListItem {
    data class Title(val data:String,val dayRange: IntRange):FileListItem()
    /**
     * File包装类，供其它文件列表中按日期分组显示使用
     * User: Donny
     * Date: 2025/08/05
     *
     * @property file           原始文件
     * @property date           所属日期（yyyy-MM-dd）
     * @property isDayFirst     是否是当天的第一个文件
     * @property dayRange       当天文件在整个列表的索引范围（左右都是闭区间）
     * @property isGroupSelected    组选中的状态
     * @property isSelected         项选中的状态
     * @constructor Create empty File with day info
     */
    data class FileWithDayInfo(
        val file: File,
        val date: String,
        val isDayFirst: Boolean,
        val dayRange: IntRange,
        var isGroupSelected: Boolean = false,
        var isSelected: Boolean = false
    ):FileListItem() {
        /**
         * 是否是音频文件
         *
         * @return
         */
        fun isAudioFile(): Boolean {
            return file.isAudioFile()
        }

        /**
         * 是否是安装包文件
         *
         * @return
         */
        fun isApkFile(): Boolean {
            return file.isApkFile()
        }
    }
}
