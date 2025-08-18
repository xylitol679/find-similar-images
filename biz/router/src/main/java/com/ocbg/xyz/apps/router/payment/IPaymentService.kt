package com.ocbg.xyz.apps.router.payment

import android.content.Context
import android.content.Intent

/**
 * 支付相关接口
 * User: Tom
 * Date: 2025/7/8 20:21
 */
interface IPaymentService {

    fun getMemberIntent(context: Context): Intent

}