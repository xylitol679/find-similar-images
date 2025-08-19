package xyz.xyzmu.sitest.apps.pc.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bumptech.glide.Glide
import com.drake.statusbar.statusPadding
import com.ocbg.xyz.apps.helper.HelperActivity
import com.ocbg.xyz.apps.router.PageNav
import xyz.xyzmu.sitest.apps.pc.home.databinding.HomeActivityHomeBinding


/**
 * 首页 Activity
 * User: Tom
 * Date: 2025/8/5 10:38
 */
internal class HomeActivity : HelperActivity<HomeActivityHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    override fun isEdgeToEdge(): Boolean {
        return true
    }

    override fun createBinding(savedInstanceState: Bundle?): HomeActivityHomeBinding {
        return HomeActivityHomeBinding.inflate(layoutInflater)
    }

    override fun initToolbar(savedInstanceState: Bundle?) {
        super.initToolbar(savedInstanceState)
        binding.toolbar.statusPadding()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.stateLayout.onRefresh {
            viewModel.scanImageFiles(32)
        }
    }

    override fun initListener() {
        super.initListener()
        binding.btnSize8.setOnClickListener {
            viewModel.scanImageFiles(8)
        }

        binding.btnSize32.setOnClickListener {
            viewModel.scanImageFiles(32)
        }

        binding.btnScanPhotos.setOnClickListener {
            // todo 扫描本地相似图片
            checkStoragePermission {
                PageNav.toDuplicateImagesPage(this)
            }
        }
    }

    override fun subscribeUi() {
        super.subscribeUi()
        viewModel.sourceImages.observe(this) {
            binding.stateLayout.showContent()
            updateSourceImagesUI(it)
        }

        viewModel.smallImages.observe(this) {
            updateSmallImagesUI(it)
        }

        viewModel.grayscaleImages.observe(this) {
            updateGrayScaleImagesUI(it)
        }
    }

    override fun loadData() {
        super.loadData()
        binding.stateLayout.showLoading()
    }

    private fun getImageView(bitmap: Bitmap): ImageView {
        return ImageView(this).apply {
            Glide.with(this)
                .load(bitmap)
                .into(this)
        }
    }

    private fun updateSourceImagesUI(list: List<Bitmap>) {
        binding.llSource.removeAllViews()
        list.forEach {
            val lp = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            lp.weight = 1f
            binding.llSource.addView(getImageView(it), lp)
        }
    }

    private fun updateSmallImagesUI(list: List<Bitmap>) {
        binding.llSmall.removeAllViews()
        list.forEach {
            val lp = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            lp.weight = 1f
            binding.llSmall.addView(getImageView(it), lp)
        }
    }

    private fun updateGrayScaleImagesUI(list: List<Bitmap>) {
        binding.llGrayscale.removeAllViews()
        list.forEach {
            val lp = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            lp.weight = 1f
            binding.llGrayscale.addView(getImageView(it), lp)
        }
    }

}