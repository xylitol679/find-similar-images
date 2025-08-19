package xyz.xyzmu.sitest.apps.pc.duplicate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.drake.statusbar.statusPadding
import com.ocbg.xyz.apps.common.entity.UiState
import com.ocbg.xyz.apps.helper.HelperActivity
import com.ocbg.xyz.apps.ui.dialog.ConfirmDialog
import kotlinx.coroutines.launch
import xyz.xyzmu.sitest.apps.pc.duplicate.adapter.ScanResultListAdapter
import xyz.xyzmu.sitest.apps.pc.duplicate.databinding.DiActivityIndexBinding
import xyz.xyzmu.sitest.apps.pc.duplicate.entity.ItemGroup

/**
 * 重复图片-扫描结果 Activity
 * User: Tom
 * Date: 2025/8/13 19:33
 */
internal class ScanResultActivity : HelperActivity<DiActivityIndexBinding>() {

    private val viewModel by viewModels<ScanResultViewModel>()

    private val listAdapter = ScanResultListAdapter()

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, ScanResultActivity::class.java)
        }

        fun launch(context: Context) {
            context.startActivity(newIntent(context))
        }
    }

    override fun isEdgeToEdge(): Boolean {
        return true
    }

    override fun createBinding(savedInstanceState: Bundle?): DiActivityIndexBinding {
        return DiActivityIndexBinding.inflate(layoutInflater)
    }

    override fun initToolbar(savedInstanceState: Bundle?) {
        super.initToolbar(savedInstanceState)
        binding.tvTitle.text = getString(R.string.di_duplicate_images)
        binding.ivBack.setOnClickListener {
            showConfirmExitDialog {
                if (it) {
                    finishAfterTransition()
                }
            }
        }
        binding.toolbar.statusPadding()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.stateLayout.onEmpty {
            findViewById<ImageView>(R.id.ivImage).setImageResource(
                R.drawable.ui_ic_empty_image
            )
            findViewById<TextView>(R.id.tvSubtitle).text =
                getString(R.string.ui_no_data_duplicate_images)
            findViewById<Button>(R.id.btnBack).setOnClickListener {
                finishAfterTransition()
            }
        }
        binding.stateLayout.onRefresh {
            viewModel.scanFiles()
        }

        (binding.rvFiles.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvFiles.setHasFixedSize(true)
        binding.rvFiles.adapter = listAdapter

        binding.btnClean.isEnabled = false
    }

    override fun initListener() {
        super.initListener()
        // 选中项监听
        listAdapter.setOnItemSelectListener { files, _ ->
            // 恢复按钮文案和可用状态
            binding.btnClean.isEnabled = files.isNotEmpty()
        }
    }

    override fun subscribeUi() {
        super.subscribeUi()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.filesUiState.collect {
                    dismissLoadingDialog()
                    updateFileListUI(it)
                }
            }
        }
    }

    private fun updateFileListUI(uiState: UiState<List<ItemGroup>>) {
        when (uiState) {
            UiState.Loading -> {
                binding.stateLayout.showLoading()
            }

            is UiState.Success -> {
                val list = uiState.data
                if (list.isNotEmpty()) {
                    binding.stateLayout.showContent()
                    listAdapter.submitList(uiState.data)
                } else {
                    binding.stateLayout.showEmpty()
                }
            }

            is UiState.Failure -> {
            }
        }
    }

    /**
     * 显示确认退出弹窗
     */
    private fun showConfirmExitDialog(block: (Boolean) -> Unit) {
        if (listAdapter.items.isEmpty()) {
            block.invoke(true)
            return
        }

        ConfirmDialog.newInstanceExit().apply {
            setOnDialogClickListener(object : ConfirmDialog.OnDialogClickListener {
                override fun onCancel() {
                    block.invoke(true)
                }

                override fun onOk() {
                    block.invoke(false)
                }
            })
        }.show(supportFragmentManager, null)
    }

    override fun onBackPressed() {
        showConfirmExitDialog {
            if (it) {
                super.onBackPressed()
            }
        }
    }

}