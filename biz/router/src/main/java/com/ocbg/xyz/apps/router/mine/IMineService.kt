package com.ocbg.xyz.apps.router.mine

import android.content.Context
import android.content.Intent

/**
 * 我的
 * User: Tom
 * Date: 2025/6/13 15:59
 */
interface IMineService {

    /**
     * 获取主页 Intent
     * @return Intent
     */
    fun getIndexIntent(context: Context): Intent

}