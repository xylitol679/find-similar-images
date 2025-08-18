import com.ocbg.xyz.apps.data.constant.FileExtensionsType
import java.io.File

/**
 * 是否是音频文件
 *
 * @return
 */
fun File.isAudioFile(): Boolean {
    return FileExtensionsType.AUDIO_EXTENSIONS.any {
        name.lowercase().endsWith(it)
    }
}

/**
 * 是否是安装包文件
 *
 * @return
 */
fun File.isApkFile(): Boolean {
    return FileExtensionsType.APK_EXTENSIONS.any {
        name.lowercase().endsWith(it)
    }
}