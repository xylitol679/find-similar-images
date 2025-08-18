package com.ocbg.xyz.apps.common.entity

/**
 * 界面状态密封类
 * User: Tom
 * Date: 2025/2/25 21:05
 */
sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Failure(val message: String?) : UiState<Nothing>
}