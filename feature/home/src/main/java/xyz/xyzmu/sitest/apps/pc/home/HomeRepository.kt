package xyz.xyzmu.sitest.apps.pc.home

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.xyzmu.sitest.apps.pc.home.util.ImageUtils

/**
 * 首页 Repository
 * User: Tom
 * Date: 2025/8/15 15:08
 */
internal class HomeRepository {

    /**
     * 获取缩小图像
     */
    suspend fun getSmallImages(sourceBitmaps: List<Bitmap>, width: Int, height: Int) =
        withContext(Dispatchers.IO) {
            val smallBitmaps = mutableListOf<Bitmap>()
            sourceBitmaps.forEach { bitmap ->
                try {
                    val smallBitmap = ImageUtils.resizeBitmap(bitmap, width, height)
                    smallBitmaps.add(smallBitmap)
                } catch (e: Exception) {
                    //
                }
            }

            return@withContext smallBitmaps
        }

    /**
     * 获取灰度图像
     */
    suspend fun getGrayscaleImages(bitmaps: List<Bitmap>) =
        withContext(Dispatchers.IO) {
            val grayScaleBitmaps = mutableListOf<Bitmap>()
            bitmaps.forEach { bmp ->
                try {
                    val bitmap = ImageUtils.toGrayscale(bmp)
                    grayScaleBitmaps.add(bitmap)
                } catch (e: Exception) {
                    //
                }
            }
            return@withContext grayScaleBitmaps
        }

}
