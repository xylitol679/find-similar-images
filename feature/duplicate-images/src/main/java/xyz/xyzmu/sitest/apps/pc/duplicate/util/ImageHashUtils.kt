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
    private fun toGrayscale(bmpOriginal: Bitmap): Bitmap {
        val width = bmpOriginal.width
        val height = bmpOriginal.height
        val bmpGrayscale = createBitmap(width, height)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val f = ColorMatrixColorFilter(cm)
        paint.colorFilter = f
        c.drawBitmap(bmpOriginal, 0f, 0f, paint)
        return bmpGrayscale
    }

    // 核心：计算aHash值
    fun calculateAHash(bitmap: Bitmap): String {
        // 1. 缩小尺寸: 简化计算，pHash常用32x32
        val smallBitmap = resizeBitmap(bitmap, 32, 32)

        // 2. 转为灰度图
        val grayscaleBitmap = toGrayscale(smallBitmap)

        // 3. 计算DCT: 这一步在Android中较复杂，通常我们会用一个简化的方式
        // 即计算所有像素的平均值，这其实更像 aHash (平均哈希)，但对于重复图片检测已足够有效
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