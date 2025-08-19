package xyz.xyzmu.sitest.apps.pc.home.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale

object ImageUtils {
    /**
     * 缩小尺寸
     */
    fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return bitmap.scale(width, height)
    }

    /**
     * 灰度化
     */
    fun toGrayscale(bitmap: Bitmap): Bitmap {
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

}