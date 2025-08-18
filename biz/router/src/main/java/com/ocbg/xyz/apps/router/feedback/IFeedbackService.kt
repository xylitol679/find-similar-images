package com.ocbg.xyz.apps.router.feedback

import android.content.Context
import android.content.Intent

/**
 * 意见反馈相关接口
 * User: Tom
 * Date: 2025/3/27 15:49
 */
interface IFeedbackService {

    fun getFeedbackIntent(context: Context): Intent

}