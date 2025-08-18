package com.ocbg.xyz.apps.common.util

/**
 * 手机号小工具类
 */
object MPhoneUtil {

    /** 通过 */
    const val STATE_PASS = 0

    /** 内容为空 */
    const val STATE_EMPTY = 1

    /** 格式有误 */
    const val STATE_FORMAT_ERROR = 2

    /**
     * 检查手机号码，默认以1开头的11位数
     * @param number 手机号
     * @return 返回结果：[STATE_PASS]，[STATE_EMPTY]，[STATE_FORMAT_ERROR]
     */
    fun checkPhoneNumber(number: String) = when {
        number.isEmpty() -> STATE_EMPTY
        !number.matches(Regex("^[1][0-9]{10}$")) -> STATE_FORMAT_ERROR
        else -> STATE_PASS
    }

    /**
     * 检查验证码，默认4位数
     * @param code 验证码
     * @return 返回结果：[STATE_PASS]，[STATE_EMPTY]，[STATE_FORMAT_ERROR]
     */
    fun checkVerificationCode(code: String) = when {
        code.isEmpty() -> STATE_EMPTY
        !code.matches(Regex("[0-9]{4}$")) -> STATE_FORMAT_ERROR
        else -> STATE_PASS
    }

}