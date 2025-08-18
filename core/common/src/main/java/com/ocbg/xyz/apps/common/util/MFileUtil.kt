package com.ocbg.xyz.apps.common.util

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.StringWriter

/**
 * File工具类
 * User: Tom
 * Date: 2023/10/20 11:27
 */
object MFileUtil {

    /**
     * 把本地图片复制私有目录
     */
    fun copyAndConvertImage(context: Context, uri: Uri, targetDir: String): File {
        return copyAndConvertImage(
            context,
            uri,
            targetDir,
            "${System.currentTimeMillis()}_copy.png"
        )
    }

    /**
     * 把本地图片复制私有目录
     */
    fun copyAndConvertImage(context: Context, uri: Uri, targetDir: String, fileName: String): File {
        var inputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        val file = File("${targetDir}/${fileName}")
        try {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            fileOutputStream = FileOutputStream(file)
            inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.let {
                copyTo(it, fileOutputStream)
            }
        } catch (exception: Exception) {
            // 空
        } finally {
            inputStream?.close()
            fileOutputStream?.close()
        }
        return file
    }

    /**
     * 把本地图片复制私有目录
     */
    fun copyFile(inputStream: InputStream?, outputPathName: String): File? {
        var fileOutputStream: FileOutputStream? = null
        val file = File(outputPathName)
        try {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            fileOutputStream = FileOutputStream(file)
            inputStream?.let {
                copyTo(it, fileOutputStream)
            }
        } catch (exception: Exception) {
            return null
        } finally {
            inputStream?.close()
            fileOutputStream?.close()
        }
        return file
    }

    private fun copyTo(inputStream: InputStream, out: OutputStream): Long {
        var bytesCopied: Long = 0
        val bufferSize = 8 * 1024
        val buffer = ByteArray(bufferSize)
        var bytes = inputStream.read(buffer)
        while (bytes >= 0) {
            out.write(buffer, 0, bytes)
            bytesCopied += bytes.toLong()
            bytes = inputStream.read(buffer)
        }
        return bytesCopied
    }

    /**
     * 创建指定的目录
     * @param path 目录路径
     * @return true表示创建成功，false表示失败
     */
    fun createDir(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }

        return try {
            val targetDir = File(path)
            if (targetDir.exists()) {
                true
            } else {
                targetDir.mkdirs()
            }
        } catch (tr: Throwable) {
            false
        }
    }

    fun createFile(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }

        return try {
            val targetFile = File(path)
            if (targetFile.exists()) {
                if (targetFile.delete()) {
                    targetFile.mkdirs()
                } else {
                    false
                }
            } else {
                targetFile.mkdirs()
            }
        } catch (tr: Throwable) {
            false
        }
    }

    /**
     * 删除指定的文件
     * @param path 文件路径
     * @return true表示删除成功，false表示失败
     */
    fun deleteFile(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }

        return try {
            File(path).deleteRecursively()
        } catch (tr: Throwable) {
            false
        }
    }

    /**
     * 检查指定的文件是否存在
     * @param path 文件路径
     * @return true表示存在，false反之
     */
    fun exists(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }

        return try {
            val file = File(path)
            file.exists() && file.isFile && file.length() > 0
        } catch (tr: Throwable) {
            false
        }
    }

    fun exists(file: File?): Boolean {
        return exists(file?.absolutePath)
    }

    /**
     * 检查指定的文件是否存在
     * @param path 文件路径
     * @return true表示存在，false反之
     */
    fun existsDir(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }

        return try {
            val file = File(path)
            file.exists() && file.isDirectory && !file.list().isNullOrEmpty()
        } catch (tr: Throwable) {
            false
        }
    }

    fun formatFileSize(sizeInBytes: Long?): String? {
        if (sizeInBytes == null) return null

        val sizeInKB = sizeInBytes / 1024
        if (sizeInKB < 1024) {
            return "${sizeInKB}KB"
        }

        val sizeInMB = sizeInKB / 1024f
        if (sizeInMB < 1024f) {
            return buildString {
                append("%.1f".format(sizeInMB))
                append("MB")
            }
        }

        val sizeInGB = sizeInMB / 1024f
        if (sizeInGB < 1024f) {
            return buildString {
                append("%.1f".format(sizeInGB))
                append("GB")
            }
        }

        return null
    }

    /**
     * 将json文件转为json字符串
     *
     * @param context 上下文
     * @param fileName json文件
     */
    suspend fun readAssetFile(context: Context, fileName: String) =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = context.assets.open(fileName)
                val reader = InputStreamReader(inputStream)
                val writer = StringWriter()
                val buffer = CharArray(1024)
                var length: Int
                while (reader.read(buffer).also { length = it } > 0) {
                    writer.write(buffer, 0, length)
                }
                writer.toString()
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }

}