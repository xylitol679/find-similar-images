package com.ocbg.xyz.apps.data.repository

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import com.ocbg.xyz.apps.common.util.MLog
import com.ocbg.xyz.apps.data.entity.FileListItem
import com.ocbg.xyz.apps.data.entity.FileListItem.FileWithDayInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.util.Date
import java.util.Locale

/**
 * 文件处理仓库类
 * User: Donny
 * Date: 2025/08/05
 *
 * @constructor Create empty File repository
 */
class FileRepository {

    companion object {
        const val TAG = "FileData"
    }

    /**
     * 搜索包含指定扩展名的所有文件
     *
     * @param file          根目录
     * @param extensions    指定扩展名集合
     * @return
     */
    suspend fun getAllFilesForIncludeExtensions(
        file: File,
        extensions: Array<String>
    ): List<File> = withContext(Dispatchers.IO) {
        try {
            if (!file.exists()) return@withContext emptyList()

            val lowerExtensions = extensions.map { it.lowercase(Locale.getDefault()) }

            fun collectFiles(dir: File): List<File> {
                return dir.listFiles().orEmpty().flatMap { singleFile ->
                    when {
                        singleFile.isDirectory -> collectFiles(singleFile)
                        lowerExtensions.any { ext ->
                            singleFile.name.lowercase(Locale.getDefault()).endsWith(ext)
                        } -> listOf(singleFile)

                        else -> emptyList()
                    }
                }
            }

            val result = collectFiles(file).sortedByDescending { it.lastModified() }
            MLog.d(
                TAG,
                "查询成功(getAllFilesForIncludeExtensions): 共${result.size}个文件。-->${result}"
            )
            result
        } catch (e: Exception) {
            e.printStackTrace()
            MLog.d(TAG, "查询过程出错(getAllFilesForIncludeExtensions): ${e.message}")
            emptyList()
        }
    }

    /**
     * 搜索包含指定扩展名的所有隐藏文件
     *
     * @param file          根目录
     * @param extensions    指定扩展名集合
     * @return
     */
    suspend fun getHiddenFilesForIncludeExtensions(
        file: File,
        extensions: Array<String>
    ): List<File> = withContext(Dispatchers.IO) {
        try {
            if (!file.exists()) return@withContext emptyList()

            val lowerExtensions = extensions.map { it.lowercase(Locale.getDefault()) }

            fun isMatchExtension(name: String): Boolean {
                return lowerExtensions.any { ext -> name.endsWith(ext) }
            }

            fun isHiddenFileOrInHiddenDir(f: File): Boolean {
                return f.isHidden || isInHiddenDir(f)
            }

            fun collectFiles(dir: File): List<File> {
                return dir.listFiles().orEmpty().flatMap { singleFile ->
                    when {
                        singleFile.isDirectory -> collectFiles(singleFile)
                        isMatchExtension(singleFile.name.lowercase(Locale.getDefault())) &&
                                isHiddenFileOrInHiddenDir(singleFile) -> listOf(singleFile)

                        else -> emptyList()
                    }
                }
            }

            val result = collectFiles(file).sortedByDescending { it.lastModified() }
            MLog.d(
                TAG,
                "查询成功(getHiddenFilesForIncludeExtensions): 共${result.size}个文件。-->${result}"
            )
            result
        } catch (e: Exception) {
            e.printStackTrace()
            MLog.d(TAG, "查询过程出错(getHiddenFilesForIncludeExtensions): ${e.message}")
            emptyList()
        }
    }

    /**
     * 搜索剔除指定扩展名的所有文件
     *
     * @param file          根目录
     * @param extensions    指定扩展名集合
     * @return
     */
    suspend fun getAllFilesForExcludeExtensions(file: File, extensions: Array<String>): List<File> =
        withContext(Dispatchers.IO) {
            try {
                if (!file.exists()) return@withContext emptyList()

                val lowerExtensions = extensions.map { it.lowercase(Locale.getDefault()) }

                fun isExcluded(name: String): Boolean {
                    val lowerName = name.lowercase(Locale.getDefault())
                    return lowerExtensions.none { lowerName.endsWith(it) }
                }

                fun collectFiles(dir: File): List<File> {
                    return dir.listFiles().orEmpty().flatMap { singleFile ->
                        when {
                            singleFile.isDirectory -> collectFiles(singleFile)
                            isExcluded(singleFile.name) -> listOf(singleFile)
                            else -> emptyList()
                        }
                    }
                }

                val result = collectFiles(file).sortedByDescending { it.lastModified() }
                MLog.d(
                    TAG,
                    "查询成功(getAllFilesForExcludeExtensions): 共${result.size}个文件。-->${result}"
                )
                result
            } catch (e: Exception) {
                e.printStackTrace()
                MLog.d(TAG, "查询过程出错(getAllFilesForExcludeExtensions): ${e.message}")
                emptyList()
            }
        }

    /**
     * 搜索剔除指定扩展名的所有隐藏文件
     *
     * @param file          根目录
     * @param extensions    指定扩展名集合
     * @return
     */
    suspend fun getHiddenFilesForExcludeExtensions(
        file: File,
        extensions: Array<String>
    ): List<File> = withContext(Dispatchers.IO) {
        try {
            if (!file.exists()) return@withContext emptyList()

            val lowerExtensions = extensions.map { it.lowercase(Locale.getDefault()) }

            fun isExcluded(f: File): Boolean {
                val lowerName = f.name.lowercase(Locale.getDefault())
                return lowerExtensions.none { lowerName.endsWith(it) }
            }

            fun isHiddenFile(f: File): Boolean {
                return f.isHidden || isInHiddenDir(f)
            }

            fun collectFiles(dir: File): List<File> {
                return dir.listFiles().orEmpty().flatMap { singleFile ->
                    when {
                        singleFile.isDirectory -> collectFiles(singleFile)
                        isExcluded(singleFile) && isHiddenFile(singleFile) -> listOf(singleFile)
                        else -> emptyList()
                    }
                }
            }

            val result = collectFiles(file).sortedByDescending { it.lastModified() }
            MLog.d(
                TAG,
                "查询成功(getHiddenFilesForExcludeExtensions): 共${result.size}个文件。-->${result}"
            )
            result
        } catch (e: Exception) {
            e.printStackTrace()
            MLog.d(TAG, "查询过程出错(getHiddenFilesForExcludeExtensions): ${e.message}")
            emptyList()
        }
    }

    /**
     * 辅助方法：判断文件是否在隐藏目录中
     *
     * @param file
     * @return
     */
    private fun isInHiddenDir(file: File): Boolean {
        var parent = file.parentFile
        while (parent != null) {
            if (parent.isHidden) return true
            parent = parent.parentFile
        }
        return false
    }

    /**
     * 将File集合转为FileWithDayInfo集合（供展示日期分组列表使用）
     *
     * @param files
     * @return
     */
    suspend fun wrapFilesWithDayInfo(files: List<File>): List<FileWithDayInfo> =
        withContext(Dispatchers.IO) {
            if (files.isEmpty()) return@withContext emptyList()

            val sdf = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())

            // 提前计算每个文件的日期，避免重复 format
            val fileDates = files.map { sdf.format(Date(it.lastModified())) }

            // 计算每个日期对应的下标范围
            val dayRanges = mutableMapOf<String, IntRange>()
            var startIndex = 0
            var currentDate = fileDates[0]

            for (i in fileDates.indices) {
                val fileDate = fileDates[i]
                if (fileDate != currentDate) {
                    dayRanges[currentDate] = startIndex until i
                    currentDate = fileDate
                    startIndex = i
                }
            }
            dayRanges[currentDate] = startIndex..files.lastIndex

            val result = files.mapIndexed { i, file ->
                val date = fileDates[i]
                val range = dayRanges.getValue(date)
                FileWithDayInfo(
                    file = file,
                    date = date,
                    isDayFirst = (i == range.first),
                    dayRange = range
                )
            }
            MLog.d(
                TAG,
                "File集合分组包装后(wrapFilesWithDayInfo): 共${result.size}个文件。-->${result}"
            )
            result
        }

    /**
     * 重新排序FileWithDayInfo集合（用于展示日期分组列表）
     *
     * @param files
     * @return
     */
    suspend fun resortFilesWithDayInfo(files: List<FileWithDayInfo>): List<FileWithDayInfo> =
        withContext(Dispatchers.IO) {
            val list = files.map {
                it.file
            }.sortedByDescending {
                it.lastModified()
            }
            wrapFilesWithDayInfo(list)
        }

    suspend fun convertToFileListItem(files: List<FileWithDayInfo>):List<FileListItem> =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<FileListItem>()
            var addTitleCount = 0
            files.forEach{  fileInfo ->
                if (fileInfo.isDayFirst) {
                    addTitleCount++
                    result.add(FileListItem.Title(
                        data = fileInfo.date,
                        dayRange = fileInfo.dayRange.first+addTitleCount..fileInfo.dayRange.last+addTitleCount
                    ))
                }
                result.add(FileListItem.FileWithDayInfo(
                    fileInfo.file,
                    fileInfo.date,
                    fileInfo.isDayFirst,
                    fileInfo.dayRange.first+addTitleCount..fileInfo.dayRange.last+addTitleCount,
                    fileInfo.isGroupSelected,
                    fileInfo.isSelected,
                ))
            }
            MLog.d(
                TAG,
                "FileWithDayInfo集合转换成FileListItem集合(convertToFileListItem): 共${result.size-addTitleCount}个文件。-->${result}"
            )
             result
        }


    /**
     * 删除指定文件集合
     *
     * @param context
     * @param files
     * @return
     */
    suspend fun deleteFiles(context: Context, files: List<File>): Boolean =
        withContext(Dispatchers.IO) {
            if (files.isEmpty()) return@withContext false

            try {
                val deletedPaths = mutableListOf<String>()

                val allDeleted = files.filter { it.exists() }
                    .all { file ->
                        runCatching {
                            file.delete().also { success ->
                                if (success) deletedPaths.add(file.absolutePath)
                            }
                        }.getOrElse {
                            it.printStackTrace()
                            MLog.d(TAG, "单个删除过程出错(deleteFiles): ${it.message}")
                            false
                        }
                    }

                if (deletedPaths.isNotEmpty()) {
                    MediaScannerConnection.scanFile(
                        context,
                        deletedPaths.toTypedArray(),
                        null,
                        null
                    )
                }

                MLog.d(TAG, "是否全部删除(deleteFiles)：$allDeleted")
                allDeleted
            } catch (e: Exception) {
                e.printStackTrace()
                MLog.d(TAG, "整体删除过程出错(deleteFiles): ${e.message}")
                false
            }
        }

    /**
     * 删除指定文件包装类(FileWithDayInfo)集合
     *
     * @param context
     * @param files
     * @return
     */
    suspend fun deleteWrapFiles(
        context: Context,
        files: List<FileWithDayInfo>
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            if (files.isEmpty()) return@withContext false

            val deletedPaths = mutableListOf<String>()

            val allDeleted = files.map { it.file }
                .filter { it.exists() }
                .all { file ->
                    runCatching {
                        file.delete().also { success ->
                            if (success) deletedPaths.add(file.absolutePath)
                        }
                    }.getOrElse {
                        it.printStackTrace()
                        MLog.d(TAG, "单个删除过程出错(deleteWrapFiles): ${it.message}")
                        false
                    }
                }

            if (deletedPaths.isNotEmpty()) {
                MediaScannerConnection.scanFile(
                    context,
                    deletedPaths.toTypedArray(),
                    null,
                    null
                )
            }

            MLog.d(TAG, "是否全部删除(deleteWrapFiles)：$allDeleted")
            allDeleted
        } catch (e: Exception) {
            e.printStackTrace()
            MLog.d(TAG, "整体删除过程出错(deleteWrapFiles): ${e.message}")
            false
        }
    }

    /**
     * 拷贝图片或视频的文件集合至恢复文件夹
     *
     * @param context
     * @param files
     * @return
     */
    suspend fun copyMediaFilesToRecoveryFolder(
        context: Context,
        files: List<File>,
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            if (files.isEmpty()) return@withContext false

            val baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val appDir = File(baseDir, context.packageName).apply {
                if (!exists()) mkdirs()
            }

            val newPaths = mutableListOf<String>()

            val allCopied = files.filter { it.exists() && it.isFile }
                .all { file ->
                    runCatching {
                        val cleanName = file.name.replaceFirst("^\\.+".toRegex(), "")

                        var targetFile = File(appDir, cleanName)
                        if (targetFile.exists()) {
                            val nameWithoutExt = targetFile.nameWithoutExtension
                            val ext = targetFile.extension
                            val timeStamp = System.nanoTime()
                            targetFile = File(appDir, if (ext.isNotEmpty()) {
                                "${nameWithoutExt}_$timeStamp.$ext"
                            } else {
                                "${nameWithoutExt}_$timeStamp"
                            })
                        }
                        FileInputStream(file).use { input ->
                            FileOutputStream(targetFile).use { output ->
                                input.copyTo(output)
                            }
                        }
                        newPaths.add(targetFile.absolutePath)
                        true
                    }.getOrElse {
                        it.printStackTrace()
                        MLog.d(
                            TAG,
                            "单个拷贝过程出错(copyMediaFilesToRecoveryFolder): ${it.message}"
                        )
                        false
                    }
                }

            if (newPaths.isNotEmpty()) {
                MediaScannerConnection.scanFile(
                    context,
                    newPaths.toTypedArray(),
                    null,
                    null
                )
            }

            MLog.d(TAG, "是否全部拷贝(copyMediaFilesToRecoveryFolder)：$allCopied")
            allCopied
        } catch (e: Exception) {
            e.printStackTrace()
            MLog.d(TAG, "整体拷贝过程出错(copyMediaFilesToRecoveryFolder): ${e.message}")
            false
        }
    }

    /**
     * 拷贝其它文件的文件集合至恢复文件夹
     *
     * @param context
     * @param files
     * @return
     */
    suspend fun copyDocumentFilesToRecoveryFolder(
        context: Context,
        files: List<FileWithDayInfo>,
    ): Boolean = withContext(Dispatchers.IO) {
        if (files.isEmpty()) return@withContext false

        val baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val appDir = File(baseDir, context.packageName).apply {
            if (!exists()) mkdirs()
        }

        val newPaths = mutableListOf<String>()

        try {
            val allCopied = files.map { it.file }
                .filter { it.exists() && it.isFile }
                .all { file ->
                    runCatching {
                        val cleanName = file.name.replaceFirst("^\\.+".toRegex(), "")

                        var targetFile = File(appDir, cleanName)
                        if (targetFile.exists()) {
                            val nameWithoutExt = targetFile.nameWithoutExtension
                            val ext = targetFile.extension
                            val timeStamp = System.nanoTime()
                            targetFile = File(appDir, if (ext.isNotEmpty()) {
                                "${nameWithoutExt}_$timeStamp.$ext"
                            } else {
                                "${nameWithoutExt}_$timeStamp"
                            })
                        }

                        FileInputStream(file).use { input ->
                            FileOutputStream(targetFile).use { output ->
                                input.copyTo(output)
                            }
                        }
                        newPaths.add(targetFile.absolutePath)
                        true
                    }.getOrElse {
                        it.printStackTrace()
                        MLog.d(
                            TAG,
                            "单个拷贝过程出错(copyDocumentFilesToRecoveryFolder): ${it.message}"
                        )
                        false
                    }
                }

            if (newPaths.isNotEmpty()) {
                MediaScannerConnection.scanFile(
                    context,
                    newPaths.toTypedArray(),
                    null,
                    null
                )
            }

            MLog.d(TAG, "是否全部拷贝(copyDocumentFilesToRecoveryFolder)：$allCopied")
            allCopied
        } catch (e: Exception) {
            e.printStackTrace()
            MLog.d(TAG, "整体拷贝过程出错(copyDocumentFilesToRecoveryFolder): ${e.message}")
            false
        }
    }

    /**
     * 扫描所有空文件夹
     *
     * @param rootDir
     * @return
     */
    suspend fun scanEmptyFolders(rootDir: File): List<File> = withContext(Dispatchers.IO) {
        val emptyFolders = mutableListOf<File>()

        if (!rootDir.exists() || !rootDir.isDirectory) return@withContext emptyFolders

        val children = rootDir.listFiles()
        if (children != null) {
            for (child in children) {
                if (child.isDirectory) {
                    // 递归扫描子目录
                    emptyFolders.addAll(scanEmptyFolders(child))
                }
            }
        } else {
            // listFiles() 返回 null，可能权限不足，直接返回空列表
            return@withContext emptyFolders
        }

        // 当前目录是否为空
        val afterChildren = rootDir.listFiles()
        if (afterChildren != null && afterChildren.isEmpty() && rootDir.canRead()) {
            emptyFolders.add(rootDir)
        }

        return@withContext emptyFolders
    }

    /**
     * 异步连续返回所有文件名
     *
     * @return
     */
    fun scanFilesFlow(): Flow<String> = flow {
        val rootDir = Environment.getExternalStorageDirectory()
        while (true) {
            scanDirectory(rootDir) { emit(it) }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun scanDirectory(dir: File, emitFile: suspend (String) -> Unit) {
        if (!dir.exists() || !dir.isDirectory) return
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory) scanDirectory(file, emitFile)
            else {
                emitFile(file.absolutePath)
                delay(300)
            }
        }
    }
}