package com.ocbg.xyz.apps.data.constant

/**
 * 各种文件类型扩展名集合
 * User: Donny
 * Date: 2025/08/06
 *
 * @constructor Create empty File extensions
 */
object FileExtensionsType {
    /**
     * 图片扩展名集合
     */
    val PIC_EXTENSIONS = arrayOf(
        ".jpg", ".jpeg",
        ".png",
        ".gif",
        ".webp",
        ".bmp",
        ".heic", ".heif",
        ".avif"
    )

    /**
     * 视频扩展名集合
     */
    val VIDEO_EXTENSIONS = arrayOf(
        ".mp4",
        ".3gp",
        ".mkv",
        ".webm",
        ".avi",
        ".mov",
        ".m4v"
    )

    /**
     * 音频扩展名集合
     */
    val AUDIO_EXTENSIONS = arrayOf(
        ".mp3",
        ".wav",
        ".aac",
        ".flac",
        ".ogg",
        ".m4a",
        ".wma",
        ".amr"
    )

    /**
     * 安装包扩展名集合
     */
    val APK_EXTENSIONS = arrayOf(
        ".apk"
    )
}