package com.ocbg.xyz.apps.router.login

import android.content.Context
import android.content.Intent

/**
 * 登录相关接口
 * User: Tom
 * Date: 2025/7/8 20:15
 */
interface ILoginService {

    fun getLoginIntent(context: Context): Intent

    fun getBindPhoneNumIntent(context: Context): Intent

}