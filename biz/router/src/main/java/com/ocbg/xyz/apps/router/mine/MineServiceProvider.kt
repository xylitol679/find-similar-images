package com.ocbg.xyz.apps.router.mine

import com.ocbg.xyz.apps.common.router.AutoServiceLoader

/**
 * 我的
 * User: Tom
 * Date: 2025/6/13 15:59
 */
object MineServiceProvider {

    /**
     * 获取我的Service对象
     * @return IMineService
     */
    fun getService(): IMineService {
        return AutoServiceLoader.load(IMineService::class.java)
    }

}