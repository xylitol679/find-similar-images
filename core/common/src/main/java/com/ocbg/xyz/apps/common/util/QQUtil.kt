package com.ocbg.xyz.apps.common.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

/**
 * QQ的小工具类
 * User: Tom
 * Date: 2023/9/28 14:19
 */
object QQUtil {

    /**
     * 查找包是否安装
     */
    fun checkApkInstalled(context: Context, packageName: String): Boolean {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * 检查是否安装了QQ
     */
    fun checkQQInstalled(context: Context): Boolean {
        return checkApkInstalled(context, "com.tencent.mobileqq")
    }

    /**
     * 检查是否安装了微信
     */
    fun checkWXInstalled(context: Context): Boolean {
        return checkApkInstalled(context, "com.tencent.mm")
    }

    /**
     * 一键加入QQ群
     * 参考 https://qun.qq.com/join.html
     */
    fun joinQQGroup(context: Context, key: String): Boolean {
        return try {
            val uriString =
                "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key"
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriString)))
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

}