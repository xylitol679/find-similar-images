package com.ocbg.xyz.apps.router.report

import com.ocbg.xyz.apps.common.router.AutoServiceLoader
import com.ocbg.xyz.apps.lxc.router.report.IReportService

/**
 * 举报 Service 对象
 * User: Tom
 * Date: 2025/3/12 20:05
 */
object ReportServiceProvider {

    /**
     * 获取举报Service对象
     * @return IReportService
     */
    fun getService(): IReportService {
        return AutoServiceLoader.load(IReportService::class.java)
    }

}