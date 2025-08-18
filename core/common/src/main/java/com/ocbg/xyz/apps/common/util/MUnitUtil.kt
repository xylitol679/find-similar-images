package com.ocbg.xyz.apps.common.util

/**
 * 有关单位转换的工具类
 * User: Donny
 * Date: 2025/01/16
 *
 */
object MUnitUtil {

    /**
     * 转换为以万为单位，保留一位小数
     *
     * @param number
     * @return
     */
    fun formatToWanUnit(number: Int): String {
        val result = number / 10000.0
        return String.format("%.1f万", result)
    }

}