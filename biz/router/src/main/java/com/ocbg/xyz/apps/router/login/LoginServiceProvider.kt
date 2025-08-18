package com.ocbg.xyz.apps.router.login

import com.ocbg.xyz.apps.common.router.AutoServiceLoader

/**
 * 登录 Service 对象
 * User: Tom
 * Date: 2025/7/8 20:17
 */
object LoginServiceProvider {

    /**
     * 获取登录 Service对象
     * @return ILoginService
     */
    fun getService(): ILoginService {
        return AutoServiceLoader.load(ILoginService::class.java)
    }

}