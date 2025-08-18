@file:JvmName("IntExt")

package com.ocbg.xyz.apps.common.ext

import android.content.res.Resources
import android.os.Build
import android.util.TypedValue


/**
 * 常用值扩展
 */

/**
 * 获取 dp 对应的 px 值
 */
val Int.dp2px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

/**
 * 获取 dp 对应的 px 值
 */
val Float.dp2px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

/**
 * 获取 sp 对应的 px 值
 */
val Int.sp2px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

/**
 * 获取 sp 对应的 px 值
 */
val Float.sp2px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

/**
 * px 转 sp
 */
val Int.px2sp
    get() = if (Build.VERSION.SDK_INT >= 34) {
        TypedValue.deriveDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
    } else {
        val metrics = Resources.getSystem().displayMetrics
        if (metrics.scaledDensity == 0f) {
            0f
        } else {
            this / metrics.scaledDensity
        }
    }

/**
 * px 转 sp
 */
val Float.px2sp
    get() = if (Build.VERSION.SDK_INT >= 34) {
        TypedValue.deriveDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
        )
    } else {
        val metrics = Resources.getSystem().displayMetrics
        this / metrics.scaledDensity
    }
