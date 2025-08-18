package com.ocbg.xyz.apps.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.drake.statusbar.statusPadding
import com.ocbg.xyz.apps.common.util.MLog
import com.ocbg.xyz.apps.ui.R
import com.ocbg.xyz.apps.ui.base.BaseUiDialogFragment
import com.ocbg.xyz.apps.ui.databinding.UiDialogPermissionBinding
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.launch

/**
 * 权限说明对话框
 * User: Tom
 * Date: 2023/11/15 10:32
 */
class PermissionDialog : BaseUiDialogFragment<UiDialogPermissionBinding>() {

    companion object {
        private const val ARG_PERMISSIONS = "PermissionDialog.permissions"
        private const val ARG_TITLE = "PermissionDialog.title"
        private const val ARG_MESSAGE = "PermissionDialog.message"
        fun newInstance(
            permissions: ArrayList<String>,
            title: String? = null,
            message: String? = null
        ): PermissionDialog {
            return PermissionDialog().apply {
                isCancelable = false
                arguments = Bundle().apply {
                    putStringArrayList(ARG_PERMISSIONS, permissions)
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                }
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.ui_FullScreenDialog2
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): UiDialogPermissionBinding {
        return UiDialogPermissionBinding.inflate(inflater, container, false)
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.window?.let { window ->
//            WindowCompat.setDecorFitsSystemWindows(window, false) // 设置全屏
//            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
//            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars()) // 隐藏system bars
//        }
//        return dialog
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.statusPadding()

        arguments?.let { args ->
            binding.tvTitle.text = args.getString(ARG_TITLE) ?: getString(R.string.ui_permissions)
            binding.tvMessage.text = args.getString(ARG_MESSAGE) ?: ""
        }

        val permissions = arguments?.getStringArrayList(ARG_PERMISSIONS)
        if (permissions.isNullOrEmpty()) {
            dismissAllowingStateLoss()
            listener?.invoke(false)
        } else {
            lifecycleScope.launch {
                requestPermissions(permissions)
            }
        }
    }

    private fun requestPermissions(permissions: List<String>) {
        PermissionX.init(this)
            .permissions(permissions)
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    getString(R.string.ui_permissions_forward_message),
                    getString(R.string.ui_ok),
                    getString(R.string.ui_cancel)
                )
            }
            .request { allGranted, _, _ ->
                dismissAllowingStateLoss()
                listener?.invoke(allGranted)
            }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            val dialog = manager.findFragmentByTag(tag)
            if (dialog == null) {
                ft.add(this, tag)
            } else {
                ft.show(dialog)
            }
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            MLog.e(e.message)
        }
    }

    private var listener: ((result: Boolean) -> Unit)? = null
    fun setOnClickListener(l: (result: Boolean) -> Unit) {
        this.listener = l
    }

}