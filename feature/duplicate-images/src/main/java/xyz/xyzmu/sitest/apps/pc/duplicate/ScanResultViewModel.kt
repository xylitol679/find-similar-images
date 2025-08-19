package xyz.xyzmu.sitest.apps.pc.duplicate

import android.app.Application
import android.os.Environment
import androidx.lifecycle.viewModelScope
import com.ocbg.xyz.apps.common.base.BaseAndroidViewModel
import com.ocbg.xyz.apps.common.entity.UiState
import com.ocbg.xyz.apps.data.constant.FileExtensionsType
import com.ocbg.xyz.apps.data.repository.FileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.xyzmu.sitest.apps.pc.duplicate.entity.ItemGroup
import xyz.xyzmu.sitest.apps.pc.duplicate.repository.DuplicateImagesRepository
import java.io.File

/**
 * 重复图片-扫描结果 ViewModel
 * User: Tom
 * Date: 2025/8/13 19:34
 */
internal class ScanResultViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val fileRepo by lazy { FileRepository() }

    private val duplicateRepo by lazy { DuplicateImagesRepository() }

    private val _filesUiState = MutableStateFlow<UiState<List<ItemGroup>>>(UiState.Loading)
    val filesUiState: StateFlow<UiState<List<ItemGroup>>> = _filesUiState.asStateFlow()

    /**
     * 扫描图片文件
     */
    fun scanFiles() {
        viewModelScope.launch {
            try {
                val dir = File(Environment.getExternalStorageDirectory().absolutePath)
                val extensions = FileExtensionsType.PIC_EXTENSIONS
                val files = fileRepo.getAllFilesForIncludeExtensions(dir, extensions)
                _filesUiState.value = duplicateRepo.findDuplicatePhotos(files)
            } catch (tr: Throwable) {
                _filesUiState.value = UiState.Failure(null)
            }
        }
    }

}