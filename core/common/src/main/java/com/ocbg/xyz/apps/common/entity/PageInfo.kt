package com.ocbg.xyz.apps.common.entity

/**
 * 分页信息，用于刷新、加载，控制页数
 * User: Tom
 * Date: 2023/9/15 11:43
 */
data class PageInfo(
    /** 页码 */
    var page: Int = 1,
    /** 每页大小 */
    var pageSize: Int = 30
) {
    /**
     * 下一页
     */
    fun nextPage() {
        page++
    }

    /**
     * 重置页数为1
     */
    fun resetPage() {
        page = 1
    }

    /**
     * 是否是首页
     *
     * @return true表示首页
     */
    val isFirstPage: Boolean
        get() = page == 1

}