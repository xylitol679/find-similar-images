package com.ocbg.xyz.apps.ui.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.ocbg.xyz.apps.common.base.BaseBottomSheetDialogFragment
import com.ocbg.xyz.apps.common.util.MToast
import com.ocbg.xyz.apps.ui.R
import com.ocbg.xyz.apps.ui.dialog.LoadingDialog
import kotlinx.coroutines.launch

/**
 * BottomSheetDialogFragment UI基类
 * User: Tom
 * Date: 2025/2/26 10:16
 */
abstract class BaseUiBottomSheetDialogFragment<T : ViewBinding> :
    BaseBottomSheetDialogFragment<T>() {

    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ui_BottomSheetDialog)
    }

    /**
     * 显示加载弹窗
     */
    protected fun showLoadingDialog(
        cancelable: Boolean = false,
        message: String? = null,
        transparent: Boolean = false
    ) {
        lifecycleScope.launch {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog(requireActivity(), cancelable, message, transparent)
            } else {
                loadingDialog?.setMyCancelable(cancelable)
                loadingDialog?.setMessage(message)
            }
            loadingDialog?.show()
        }
    }

    /**
     * 关闭加载弹窗
     */
    protected fun dismissLoadingDialog() {
        lifecycleScope.launch {
            loadingDialog?.dismiss()
        }
    }

    protected fun showToast(text: String?) {
        lifecycleScope.launch {
            MToast.show(requireActivity(), text)
        }
    }

    protected fun showToast(@StringRes resId: Int) {
        lifecycleScope.launch {
            MToast.show(requireActivity(), resId)
        }
    }

}