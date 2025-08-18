package com.ocbg.xyz.apps.router.payment

import com.ocbg.xyz.apps.common.router.AutoServiceLoader

/**
 * 支付 Service 对象
 * User: Tom
 * Date: 2025/7/8 20:22
 */
object PaymentServiceProvider {

    /**
     * 获取支付 Service对象
     * @return IPaymentService
     */
    fun getService(): IPaymentService {
        return AutoServiceLoader.load(IPaymentService::class.java)
    }

}