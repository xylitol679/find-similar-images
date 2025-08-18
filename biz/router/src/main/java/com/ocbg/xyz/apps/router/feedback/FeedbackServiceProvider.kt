package com.ocbg.xyz.apps.router.feedback

import com.ocbg.xyz.apps.common.router.AutoServiceLoader

/**
 * 意见反馈 Service 对象
 * User: Tom
 * Date: 2025/3/27 15:50
 */
object FeedbackServiceProvider {

    /**
     * 获取意见反馈Service对象
     * @return IFeedbackService
     */
    fun getService(): IFeedbackService {
        return AutoServiceLoader.load(IFeedbackService::class.java)
    }

}