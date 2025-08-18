package com.ocbg.xyz.apps.common.entity

/**
 * 分页
 * User: Tom
 * Date: 2023/9/15 11:43
 */
data class Paging<T>(
    val totalSize: Int,
    val totalPageCount: Int,
    val pageIndex: Int,
    val pageSize: Int,
    val list: List<T>?
)