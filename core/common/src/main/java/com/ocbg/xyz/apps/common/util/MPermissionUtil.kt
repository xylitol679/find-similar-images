package com.ocbg.xyz.apps.common.util

import android.Manifest
import android.content.Context
import android.os.Build
import com.permissionx.guolindev.PermissionX

/**
 * 权限工具类
 * User: Tom
 * Date: 2023/11/15 14:08
 */
object MPermissionUtil {

    val readMediaImages by lazy {
        if (Build.VERSION.SDK_INT >= 33) {
            arrayListOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    val readMediaVideo by lazy {
        if (Build.VERSION.SDK_INT >= 33) {
            arrayListOf(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    val readMediaImagesVideo by lazy {
        if (Build.VERSION.SDK_INT >= 33) {
            arrayListOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    val camera by lazy {
        arrayListOf(
            Manifest.permission.CAMERA
        )
    }

    val accessLocation by lazy {
        arrayListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    val requestInstallPackages by lazy {
        arrayListOf(Manifest.permission.REQUEST_INSTALL_PACKAGES)
    }

    /**
     * 检查权限是否授权
     * @param context
     * @param permissions 待检查的权限
     * @return true表示已授权，false反之
     */
    fun isGranted(context: Context, permissions: List<String>): Boolean {
        return permissions.map { PermissionX.isGranted(context, it) }.find { !it } == null
    }

    /**
     * 判断读取媒体照片的权限是否授予
     * @return true表示已授权，false反之
     */
    fun isGrantedReadMediaImages(context: Context): Boolean {
        return isGranted(context, readMediaImages)
    }

    /**
     * 判断读取媒体视频的权限是否授予
     * @return true表示已授权，false反之
     */
    fun isGrantedReadMediaVideo(context: Context): Boolean {
        return isGranted(context, readMediaVideo)
    }

    /**
     * 判断读取媒体照片和视频的权限是否授予
     * @return true表示已授权，false反之
     */
    fun isGrantedReadMediaImagesVideo(context: Context): Boolean {
        return isGranted(context, readMediaImagesVideo)
    }

    /**
     * 判断请求获取安装包的权限是否授予
     * @return true表示已授权，false反之
     */
    fun canRequestPackageInstalls(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return context.packageManager.canRequestPackageInstalls()
        }
        // API 25 及以下默认允许安装
        return true;
    }

    /**
     * 判断相机的权限是否授予
     * @return true表示已授权，false反之
     */
    fun isGrantedCamera(context: Context): Boolean {
        return isGranted(context, camera)
    }

    /**
     * 判断精确位置的权限是否授予
     * @return true表示已授权，false反之
     */
    fun isGrantedAccessLocation(context: Context): Boolean {
        return isGranted(context, accessLocation)
    }

}