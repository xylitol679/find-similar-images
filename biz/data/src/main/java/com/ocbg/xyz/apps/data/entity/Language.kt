package com.ocbg.xyz.apps.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 语言
 * User: Tom
 * Date: 2025/7/25 14:52
 */
@Parcelize
data class Language(
    val subtag: String?,
    val description: String,
    val icon: Int
) : Parcelable