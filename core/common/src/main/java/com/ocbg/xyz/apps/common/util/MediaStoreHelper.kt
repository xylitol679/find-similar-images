package com.ocbg.xyz.apps.common.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.IOException

/**
 * 媒体库助手
 * User: Tom
 * Date: 2023/10/20 11:28
 */
object MediaStoreHelper {

    private const val TAG: String = "MediaStoreHelper"

    /**
     * 通过视频的名称判断媒体库是否存在同名的视频
     *
     * @param displayName 文件名，包含后缀，如 xxx.mp4
     * @return 有则返回Uri，无则返回空Uri
     */
    fun isVideoExist(context: Context, displayName: String): Uri {
        val projection = arrayOf(MediaStore.Video.Media._ID)
        val selection = MediaStore.Video.Media.DISPLAY_NAME + " = ?"
        val selectionArgs = arrayOf(displayName)
        val sortOrder = null

        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        ) ?: return Uri.EMPTY
        if (!cursor.moveToFirst()) {
            return Uri.EMPTY
        }
        val id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
        val uri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
        cursor.close()
        return uri
    }

    /**
     * 通过图片的名称判断媒体库是否存在同名的图片
     *
     * @param displayName 文件名，包含后缀，如 xxx.mp4
     * @return 有则返回Uri，无则返回空Uri
     */
    fun isImageExist(context: Context, displayName: String): Uri {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = MediaStore.Images.Media.DISPLAY_NAME + " = ?"
        val selectionArgs = arrayOf(displayName)
        val sortOrder = null

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        ) ?: return Uri.EMPTY
        if (!cursor.moveToFirst()) {
            return Uri.EMPTY
        }
        val id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
        val uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        cursor.close()
        return uri
    }

    /**
     * 通过视频的名称获取对应的Uri
     *
     * @param displayName 文件名，包含后缀，如 xxx.mp4
     * @return 找到则返回Uri，反之null
     */
    fun getVideoUri(context: Context, displayName: String): Uri? {
        val projection = arrayOf(MediaStore.Video.Media._ID)
        val selection = MediaStore.Video.Media.DISPLAY_NAME + " = ?"
        val selectionArgs = arrayOf(displayName)
        val sortOrder = null

        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        ) ?: return null
        if (!cursor.moveToFirst()) {
            return null
        }
        val id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
        val uri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
        cursor.close()
        return uri
    }

    /**
     * 将图片存储到本地媒体库
     *
     * @param displayName 文件名，包含后缀，如 xxx.png
     * @param mimeType    媒体类型
     * @return 已添加的图片的Uri
     */
    fun createImageUri(
        context: Context,
        displayName: String,
        mimeType: String?,
        relativePath: String,
        description: String?
    ): Uri? {
        var uri: Uri? = null
        try {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.DESCRIPTION, description)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            } else {
                values.put(
                    MediaStore.MediaColumns.DATA,
                    Environment.getExternalStorageDirectory().path + "/" + relativePath + "/" + displayName
                )
            }
            uri =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } catch (e: IOException) {
            Log.e(TAG, "存储图片异常: $e")
        }
        return uri
    }

}