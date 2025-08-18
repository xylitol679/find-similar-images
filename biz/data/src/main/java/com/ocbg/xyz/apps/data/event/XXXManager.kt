package com.ocbg.xyz.apps.data.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * XXX事件管理类
 * User: Tom
 * Date: 2025/2/24 16:06
 */
object XXXManager {

    // 非粘性，仅缓冲最新的一个值
    private val _events = MutableSharedFlow<XXXEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    fun sendEvent(scope: CoroutineScope, event: XXXEvent) {
        scope.launch {
            _events.emit(event)
        }
    }

    fun quickSendEvent(event: XXXEvent) {
        _events.tryEmit(event)
    }
}