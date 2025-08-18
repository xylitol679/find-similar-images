package com.ocbg.xyz.apps.common.ext

import android.view.View
import com.ocbg.xyz.apps.common.ext.ViewClickDelay.SPACE_TIME
import com.ocbg.xyz.apps.common.ext.ViewClickDelay.hash
import com.ocbg.xyz.apps.common.ext.ViewClickDelay.lastClickTime
import java.math.BigDecimal


object ViewClickDelay {
    var hash: Int = 0
    var lastClickTime: Long = 0
    var SPACE_TIME: Long = 1000
}

/**
 * 防止连续点击
 * @receiver View
 * @param clickAction Function0<Unit>
 */
infix fun View.clickDelay(clickAction: () -> Unit) {
    this.setOnClickListener {
        if (this.hashCode() != hash) {
            hash = this.hashCode()
            lastClickTime = System.currentTimeMillis()
            clickAction()
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > SPACE_TIME) {
                lastClickTime = System.currentTimeMillis()
                clickAction()
            }
        }
    }
}

/**
 * float相加，不丢失精度
 */
fun Float.add(other: Float): Float {
    return BigDecimal(this.toString()).add(BigDecimal(other.toString())).toFloat()
}

/**
 * float相减，不丢失精度
 */
fun Float.subtract(other: Float): Float {
    return BigDecimal(this.toString()).subtract(BigDecimal(other.toString())).toFloat()
}

/**
 * Int乘以Float，不丢失精度
 */
fun Int.multiply(other: Float): Float {
    return BigDecimal(this.toString()).multiply(BigDecimal(other.toString())).toFloat()
}



