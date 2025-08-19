package xyz.xyzmu.sitest.apps.pc.home

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ocbg.xyz.apps.common.base.BaseAndroidViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 首页 ViewModel
 * User: Tom
 * Date: 2025/8/13 19:34
 */
internal class HomeViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val repo by lazy { HomeRepository() }

    private val _sourceImages = MutableLiveData<List<Bitmap>>()
    val sourceImages: LiveData<List<Bitmap>> get() = _sourceImages

    private val _smallImages = MutableLiveData<List<Bitmap>>()
    val smallImages: LiveData<List<Bitmap>> get() = _smallImages

    private val _grayscaleImages = MutableLiveData<List<Bitmap>>()
    val grayscaleImages: LiveData<List<Bitmap>> get() = _grayscaleImages

    /**
     * 扫描图片文件
     */
    fun scanImageFiles(size: Int) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>()
                val sourceBitmaps = listOf(
//                    BitmapFactory.decodeResource(context.resources, R.drawable.ui_img_test_bird_1),
//                    BitmapFactory.decodeResource(context.resources, R.drawable.ui_img_test_bird_2)
                    BitmapFactory.decodeResource(context.resources, R.drawable.ui_test_cat),
                    BitmapFactory.decodeResource(context.resources, R.drawable.ui_test_dog)
                )
                _sourceImages.value = sourceBitmaps

                delay(200)
                val smallBitmaps = repo.getSmallImages(sourceBitmaps, size, size)
                _smallImages.value = smallBitmaps

                delay(200)
                val grayScaleImages = repo.getGrayscaleImages(smallBitmaps)
                _grayscaleImages.value = grayScaleImages
            } catch (tr: Throwable) {
                _sourceImages.value = emptyList()
            }
        }
    }

}