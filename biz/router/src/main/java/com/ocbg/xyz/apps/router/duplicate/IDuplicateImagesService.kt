package com.ocbg.xyz.apps.router.duplicate

import android.content.Context
import android.content.Intent

/**
 * 重复图像
 * User: Tom
 * Date: 2025/8/13 19:36
 */
interface IDuplicateImagesService {

    /**
     * 获取主页 Intent
     * @return Intent
     */
    fun getIndexIntent(context: Context): Intent

}