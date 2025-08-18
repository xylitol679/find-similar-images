package com.ocbg.xyz.apps.common.util

import com.drake.logcat.LogCat

/**
 * 日志，封装其他日志库
 * User: Tom
 * Date: 2023/10/20 14:57
 */
object MLog {

    private var tag = "日志"

    /**
     * @param enabled 是否启用日志
     * @param tag 日志默认标签
     */
    fun setDebug(enabled: Boolean, tag: String = MLog.tag) {
        LogCat.setDebug(enabled, tag)
    }

    fun v(
        msg: Any?
    ) {
        v(tag, msg, null, null)
    }

    fun v(
        tag: String,
        msg: Any?,
    ) {
        v(tag, msg, null, null)
    }

    fun v(
        tag: String,
        msg: Any?,
        tr: Throwable?,
    ) {
        v(tag, msg, tr, null)
    }

    fun v(
        tag: String,
        msg: Any?,
        tr: Throwable?,
        occurred: Throwable?
    ) {
        LogCat.v(msg, tag, tr, occurred)
    }

    fun d(
        msg: Any?
    ) {
        d(tag, msg, null, null)
    }

    fun d(
        tag: String,
        msg: Any?,
    ) {
        d(tag, msg, null, null)
    }

    fun d(
        tag: String,
        msg: Any?,
        tr: Throwable?,
    ) {
        d(tag, msg, tr, null)
    }

    fun d(
        tag: String,
        msg: Any?,
        tr: Throwable?,
        occurred: Throwable?
    ) {
        LogCat.d(msg, tag, tr, occurred)
    }

    fun i(
        msg: Any?
    ) {
        i(tag, msg, null, null)
    }

    fun i(
        tag: String,
        msg: Any?,
    ) {
        i(tag, msg, null, null)
    }

    fun i(
        tag: String,
        msg: Any?,
        tr: Throwable?,
    ) {
        i(tag, msg, tr, null)
    }

    fun i(
        tag: String,
        msg: Any?,
        tr: Throwable?,
        occurred: Throwable?
    ) {
        LogCat.i(msg, tag, tr, occurred)
    }

    fun w(
        msg: Any?
    ) {
        w(tag, msg, null, null)
    }

    fun w(
        tag: String,
        msg: Any?,
    ) {
        w(tag, msg, null, null)
    }

    fun w(
        tag: String,
        msg: Any?,
        tr: Throwable?,
    ) {
        w(tag, msg, tr, null)
    }

    fun w(
        tag: String,
        msg: Any?,
        tr: Throwable?,
        occurred: Throwable?
    ) {
        LogCat.w(msg, tag, tr, occurred)
    }

    fun e(
        msg: Any?
    ) {
        e(tag, msg, null, null)
    }

    fun e(
        tag: String,
        msg: Any?,
    ) {
        e(tag, msg, null, null)
    }

    fun e(
        tag: String,
        msg: Any?,
        tr: Throwable?,
    ) {
        e(tag, msg, tr, null)
    }

    fun e(
        tag: String,
        msg: Any?,
        tr: Throwable?,
        occurred: Throwable?
    ) {
        LogCat.e(msg, tag, tr, occurred)
    }

    fun wtf(
        msg: Any?
    ) {
        wtf(tag, msg, null, null)
    }

    fun wtf(
        tag: String,
        msg: Any?,
    ) {
        wtf(tag, msg, null, null)
    }

    fun wtf(
        tag: String,
        msg: Any?,
        tr: Throwable?,
    ) {
        wtf(tag, msg, tr, null)
    }

    fun wtf(
        tag: String,
        msg: Any?,
        tr: Throwable?,
        occurred: Throwable?
    ) {
        LogCat.wtf(msg, tag, tr, occurred)
    }

    /**
     * JSON格式化输出日志
     */
    fun json(
        tag: String,
        json: Any?
    ) {
        LogCat.json(json, tag)
    }

}