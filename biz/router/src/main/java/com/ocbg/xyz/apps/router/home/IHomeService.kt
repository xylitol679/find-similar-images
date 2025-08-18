package com.ocbg.xyz.apps.router.home

import android.content.Context
import android.content.Intent

/**
 * 主页
 * User: Tom
 * Date: 2025/6/13 11:47
 */
interface IHomeService {

    /**
     * 获取主页 Intent
     * @return Intent
     */
    fun getIndexIntent(context: Context): Intent

}