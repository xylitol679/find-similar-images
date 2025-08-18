package com.ocbg.xyz.apps.router.home

import com.ocbg.xyz.apps.common.router.AutoServiceLoader

/**
 * 主页
 * User: Tom
 * Date: 2025/6/13 11:48
 */
object HomeServiceProvider {

    /**
     * 获取主页Service对象
     * @return IHomeService
     */
    fun getService(): IHomeService {
        return AutoServiceLoader.load(IHomeService::class.java)
    }

}