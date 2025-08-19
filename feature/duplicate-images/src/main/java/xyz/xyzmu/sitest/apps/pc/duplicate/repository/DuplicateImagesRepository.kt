package xyz.xyzmu.sitest.apps.pc.duplicate.repository

import android.graphics.BitmapFactory
import com.ocbg.xyz.apps.common.entity.UiState
import com.ocbg.xyz.apps.common.util.MLog
import com.ocbg.xyz.apps.data.constant.LogTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.xyzmu.sitest.apps.pc.duplicate.entity.ItemGroup
import xyz.xyzmu.sitest.apps.pc.duplicate.entity.SelectableItem
import xyz.xyzmu.sitest.apps.pc.duplicate.util.ImageHashUtils
import java.io.File

/**
 * 相似图片 Repository
 * User: Tom
 * Date: 2025/8/15 15:08
 */
internal class DuplicateImagesRepository {

    /**
     * 查找相似图片
     * @return 返回保存结果。Result.success表示成功，并附带图片Uri，Result.failure表示失败。
     */
    suspend fun findDuplicatePhotos(imageFiles: List<File>) = withContext(Dispatchers.IO) {
        try {
            val duplicatePhotos = findDuplicateImages(imageFiles)
            val groupList = covertItemGroupList(duplicatePhotos)
            MLog.d(LogTag.DUPLICATE_IMAGES, "查找相似图片 ${duplicatePhotos.size} 组")
            UiState.Success(groupList)
        } catch (tr: Throwable) {
            MLog.e(LogTag.DUPLICATE_IMAGES, "查找相似图片异常 ${tr.message}")
            UiState.Failure("查找相似图片异常")
        }
    }

    /**
     * 查找相似图片
     * @param imageFiles 待查找的图片文件
     */
    private suspend fun findDuplicateImages(imageFiles: List<File>) =
        withContext(Dispatchers.IO) {
            // 使用 HashMap 来存储 <哈希值, 图片路径列表>
            val hashToPathsMap = mutableMapOf<String, MutableList<File>>()

            // 在IO线程中进行计算密集型操作
            imageFiles.forEach { file ->
                try {
                    val path = file.absolutePath
                    // 为了避免OOM，需要对Bitmap进行缩放加载
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 8 // 缩放为1/8
                    val bitmap = BitmapFactory.decodeFile(path, options)

                    if (bitmap != null) {
                        val hash = ImageHashUtils.calculateAHash(bitmap)
                        bitmap.recycle() // 及时回收

                        if (!hashToPathsMap.containsKey(hash)) {
                            hashToPathsMap[hash] = mutableListOf()
                        }
                        hashToPathsMap[hash]?.add(file)
                    }
                } catch (e: Exception) {
                    // 处理加载失败的图片
                }
            }

            // 过滤出真正重复的图片（一个哈希值对应了多个路径）
            val duplicateGroups = hashToPathsMap.filter { it.value.size > 1 }

            return@withContext duplicateGroups
        }

    /**
     * Map转换成列表可识别的List
     */
    private fun covertItemGroupList(map: Map<String, MutableList<File>>): List<ItemGroup> {
        var groupIndex = 0
        return buildList<ItemGroup> {
            map.forEach { (_, v) ->
                add(
                    ItemGroup(
                        index = groupIndex,
                        items = emptyList(),
                        isSelected = false
                    )
                )
                add(
                    ItemGroup(
                        index = groupIndex,
                        items = buildList {
                            v.forEach { add(SelectableItem(it, false)) }
                        },
                        isSelected = false
                    )
                )
                groupIndex++
            }
        }
    }


}
