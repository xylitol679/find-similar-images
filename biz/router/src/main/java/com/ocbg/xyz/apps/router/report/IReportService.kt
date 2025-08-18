package com.ocbg.xyz.apps.lxc.router.report

import android.content.Context
import android.content.Intent

/**
 * 举报相关接口
 * User: Tom
 * Date: 2025/3/12 20:04
 */
interface IReportService {

    fun getReportIntent(context: Context, taskId: String): Intent

}