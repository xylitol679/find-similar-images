package com.ocbg.xyz.apps.common.entity

/**
 * 请求结果
 * User: Tom
 * Date: 2023/9/15 11:43
 */
data class Result<T>(
    val code: String?,
    val msg: String?,
    val url: String?,
    val data: T?
)