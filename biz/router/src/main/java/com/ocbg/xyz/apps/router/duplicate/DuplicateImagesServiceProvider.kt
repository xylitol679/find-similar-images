package com.ocbg.xyz.apps.router.duplicate

import com.ocbg.xyz.apps.common.router.AutoServiceLoader

/**
 * 重复图像
 * User: Tom
 * Date: 2025/8/13 19:36
 */
object DuplicateImagesServiceProvider {

    /**
     * 获取重复图像页 Service对象
     * @return IOtherFilesService
     */
    fun getService(): IDuplicateImagesService {
        return AutoServiceLoader.load(IDuplicateImagesService::class.java)
    }

}