package com.ocbg.xyz.apps.router

import android.content.Context
import android.content.Intent
import com.ocbg.xyz.apps.router.home.HomeServiceProvider
import com.ocbg.xyz.apps.router.duplicate.DuplicateImagesServiceProvider

/**
 * 页面导航，导航到各功能的主页
 * User: Tom
 * Date: 2025/3/31 16:01
 */
object PageNav {

    /**
     * 跳转到首页
     */
    fun toHomePage(context: Context) {
        context.startActivity(HomeServiceProvider.getService().getIndexIntent(context))
    }

    /**
     * 返回主页，跳转到首页并清除顶部所有Activity
     */
    fun toHomePageAndClearTop(context: Context) {
        context.startActivity(
            HomeServiceProvider.getService().getIndexIntent(context).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
    }

    /**
     * 跳转到重复图像页
     */
    fun toDuplicateImagesPage(context: Context) {
        context.startActivity(DuplicateImagesServiceProvider.getService().getIndexIntent(context))
    }

}