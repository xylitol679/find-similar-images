package com.ocbg.xyz.apps.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import androidx.core.net.toFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.OutputStream

/**
 * Bitmap工具类
 * User: Tom
 * Date: 2024/1/3 09:33
 */
object MBitmapUtil {

    /**
     * Base64转Bitmap
     */
    suspend fun base64ToBitmap(str: String): Bitmap? =
        withContext(Dispatchers.IO) {
            try {
                val bitmapByte = Base64.decode(str, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.size)
            } catch (tr: Throwable) {
                null
            }
        }

    suspend fun saveBitmap2Local(bitmap: Bitmap, outputStream: OutputStream): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val bos = BufferedOutputStream(outputStream)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
                bos.flush()
                bos.close()
                true
            } catch (tr: Throwable) {
                false
            }
        }

    suspend fun saveBitmap2Local(bmpStr: String, outputPath: String): File? {
        val bmp = base64ToBitmap(bmpStr) ?: return null
        return saveBitmap2Local(bmp, outputPath)
    }

    suspend fun saveBitmap2Local(bitmap: Bitmap?, outputPath: String): File? {
        bitmap ?: return null
        val file: File?
        try {
            file = File(outputPath)
        } catch (tr: Throwable) {
            return null
        }
        return if (saveBitmap2Local(bitmap, file.outputStream())) file else null
    }

    fun decodeBitmapFromFile(imagePath: String?, requestWidth: Int, requestHeight: Int): Bitmap? {
        try {
            if (!imagePath.isNullOrEmpty()) {
                if (requestWidth <= 0 || requestHeight <= 0) {
                    val bitmap = BitmapFactory.decodeFile(imagePath)
                    return rotateImage(bitmap, imagePath)
                }

                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true   // 不加载图片到内存，仅获得图片宽高
                BitmapFactory.decodeFile(imagePath, options)
                if (options.outHeight == -1 || options.outWidth == -1) {
                    try {
                        val exifInterface = ExifInterface(imagePath)
                        // 获取图片的高度
                        val height = exifInterface.getAttributeInt(
                            ExifInterface.TAG_IMAGE_LENGTH,
                            ExifInterface.ORIENTATION_NORMAL
                        )
                        // 获取图片的宽度
                        val width = exifInterface.getAttributeInt(
                            ExifInterface.TAG_IMAGE_WIDTH,
                            ExifInterface.ORIENTATION_NORMAL
                        )

                        options.outWidth = width
                        options.outHeight = height
                    } catch (tr: Throwable) {
                        // kong
                    }
                }

                // 计算获取新的采样率
                options.inSampleSize = calculateInSampleSize(
                    options,
                    requestWidth,
                    requestHeight
                )
                options.inJustDecodeBounds = false
                return rotateImage(BitmapFactory.decodeFile(imagePath, options), imagePath)

            } else {
                return null
            }
        } catch (tr: Throwable) {
            return null
        }
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        while (height / inSampleSize >= reqHeight || width / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
        return inSampleSize
    }

    fun rotateImage(bitmap: Bitmap?, path: String?): Bitmap? {
        try {
            if (bitmap == null) return null
            if (path.isNullOrEmpty()) return null
            var rotate = 0
            val exif = ExifInterface(path)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
            if (rotate == 0) return bitmap
            val matrix = Matrix()
            matrix.postRotate(rotate.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (tr: Throwable) {
            return null
        }
    }

    fun rotateAndScaleImage(bitmap: Bitmap?, path: String?, scale: Float): Bitmap? {
        try {
            if (bitmap == null) return null
            if (path.isNullOrEmpty()) return null
            var rotate = 0
            val exif = ExifInterface(path)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
            if (rotate == 0) return scaleImage(bitmap, scale)
            val matrix = Matrix()
            matrix.postScale(scale, scale)
            matrix.postRotate(rotate.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (tr: Throwable) {
            return null
        }
    }

    fun scaleImage(bitmap: Bitmap?, scale: Float): Bitmap? {
        try {
            if (bitmap == null) return null
            val matrix = Matrix()
            matrix.postScale(scale, scale)
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (tr: Throwable) {
            return null
        }
    }

    fun getBitmap(context: Context, uri: Uri): Bitmap? {
        val fd = context.contentResolver.openFileDescriptor(uri, "r")
        val bitmap = BitmapFactory.decodeFileDescriptor(fd?.fileDescriptor)
        fd?.close()
        return rotateImage(bitmap, uri.toFile().absolutePath)
    }

}