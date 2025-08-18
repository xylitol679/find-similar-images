package com.ocbg.xyz.apps.data.event

/**
 * XXX事件
 * User: Tom
 * Date: 2025/2/24 16:05
 */
sealed class XXXEvent {
    /** ZZZ有变动 */
    object ZZZChanged : XXXEvent()

    /** YYY有变动 */
    object YYYChanged : XXXEvent()
}