package com.ocbg.xyz.apps.data.repository

import com.ocbg.xyz.apps.data.event.XXXEvent
import com.ocbg.xyz.apps.data.event.XXXManager
import kotlinx.coroutines.CoroutineScope

/**
 * 全局共享事件存储类
 * User: Tom
 * Date: 2025/2/24 16:06
 */
class ShareRepository {

    /** 考预测分变更的事件 */
    val xxxEvents = XXXManager.events

    fun sendXXXEvent(scope: CoroutineScope, event: XXXEvent) {
        XXXManager.sendEvent(scope, event)
    }

    fun quickSendXXXEvent(event: XXXEvent) {
        XXXManager.quickSendEvent(event)
    }

}