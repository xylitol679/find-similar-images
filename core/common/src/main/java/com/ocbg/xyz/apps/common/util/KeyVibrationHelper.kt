package com.ocbg.xyz.apps.common.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission

/**
 * 按键震动辅助类
 * User: Tom
 * Date: 2025/7/29 10:42
 */
class KeyVibrationHelper(context: Context) {

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    /**
     * 触发短暂震动
     *
     * @param milliseconds 震动持续时长，单位毫秒。默认100毫秒。
     * @param amplitude 震动强度，Android 8.0 (API 26) 及以上版本支持。默认[VibrationEffect.DEFAULT_AMPLITUDE]。
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    fun triggerVibration(
        milliseconds: Long = 100,
        amplitude: Int = VibrationEffect.DEFAULT_AMPLITUDE
    ) {
        try {
            if (!vibrator.hasVibrator()) {
                // 设配不支持震动
                return
            }

            // 根据 API 版本选择不同的震动方法
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26 (Android O) 及以上版本
                // 震动 100 毫秒，使用默认强度
                val vibrationEffect = VibrationEffect.createOneShot(milliseconds, amplitude)
                vibrator.vibrate(vibrationEffect)
            } else {
                // API 26 以下的旧版本
                // 震动 100 毫秒
                @Suppress("DEPRECATION")
                vibrator.vibrate(milliseconds)
            }
        } catch (tr: Throwable) {
            tr.printStackTrace()
        }
    }

}