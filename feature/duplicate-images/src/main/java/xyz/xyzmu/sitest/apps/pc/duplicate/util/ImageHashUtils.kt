package xyz.xyzmu.sitest.apps.pc.duplicate.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale

object ImageHashUtils {

    // 1. 缩小图片尺寸
    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return bitmap.scale(width, height)
    }

    // 2. 灰度化
    private fun toGrayscale(bitmap: Bitmap): Bitmap {
        // 获取原 Bitmap 尺寸，创建灰度图 Bitmap
        val width = bitmap.width
        val height = bitmap.height
        val grayscaleBitmap = createBitmap(width, height)

        // 创建画布，在 grayscaleBitmap 上绘制灰度图像
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorFilter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return grayscaleBitmap
    }

    // 3. 核心：计算aHash值
    fun calculateAHash(bitmap: Bitmap): String {
        // 1. 缩小尺寸: 简化计算，aHash常用32x32
        val smallBitmap = resizeBitmap(bitmap, 32, 32)

        // 2. 转为灰度图
        val grayscaleBitmap = toGrayscale(smallBitmap)

        // 3. 即计算所有像素的平均值，对于重复图片检测已足够有效
        val pixels = IntArray(32 * 32)
        grayscaleBitmap.getPixels(pixels, 0, 32, 0, 0, 32, 32)

        var total = 0
        for (i in 0 until 32 * 32) {
            // 获取像素的R,G,B值，因为是灰度图，三者相等，取一个即可
            total += pixels[i] and 0xFF
        }
        val avg = total / (32 * 32)

        // 4. 计算哈希: 像素值大于等于平均值为1，小于为0
        val hash = StringBuilder()
        for (i in 0 until 32 * 32) {
            if ((pixels[i] and 0xFF) >= avg) {
                hash.append('1')
            } else {
                hash.append('0')
            }
        }
        return hash.toString()
    }

    // 比较两个哈希值的汉明距离
    fun hammingDistance(hash1: String, hash2: String): Int {
        if (hash1.length != hash2.length) {
            return -1
        }
        var distance = 0
        for (i in hash1.indices) {
            if (hash1[i] != hash2[i]) {
                distance++
            }
        }
        return distance
    }

}