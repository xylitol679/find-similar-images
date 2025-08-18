package com.ocbg.xyz.apps.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.ocbg.xyz.apps.common.base.BaseActivity
import com.ocbg.xyz.apps.common.util.MToast
import com.ocbg.xyz.apps.ui.dialog.LoadingDialog
import kotlinx.coroutines.launch

/**
 * Activity UI基类
 * User: Tom
 * Date: 2025/2/26 10:15
 */
abstract class BaseUiActivity<T : ViewBinding> : BaseActivity<T>() {

    private var loadingDialog: LoadingDialog? = null

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
                loadingDialog =
                    LoadingDialog(this@BaseUiActivity, cancelable, message, transparent)
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
            MToast.show(this@BaseUiActivity, text)
        }
    }

    protected fun showToast(@StringRes resId: Int) {
        lifecycleScope.launch {
            MToast.show(this@BaseUiActivity, resId)
        }
    }

}