package com.ocbg.xyz.apps.helper

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.ocbg.xyz.apps.common.util.MLog
import com.ocbg.xyz.apps.common.util.MToast
import com.ocbg.xyz.apps.data.StorageUtil
import com.ocbg.xyz.apps.data.constant.LogTag
import com.ocbg.xyz.apps.ui.base.BaseUiFragment
import com.ocbg.xyz.apps.ui.dialog.PermissionHintDialog
import com.permissionx.guolindev.PermissionX

/**
 * 助手Fragment
 * User: Tom
 * Date: 2024/3/14 16:38
 */
abstract class HelperFragment<T : ViewBinding> : BaseUiFragment<T>() {

    /** 存储权限申请成功后，需要执行的 block 代码块。 */
    private var block: (() -> Unit)? = null

    @RequiresApi(Build.VERSION_CODES.R)
    private val launcherAllFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            if (Environment.isExternalStorageManager()) {
                block?.invoke()
            } else {
                MToast.show(requireContext(), getString(R.string.helper_permission_grant_fail))
            }
        }
    private val settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // 用户从设置页返回
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                block?.invoke()
            } else {
                MToast.show(requireContext(), getString(R.string.helper_permission_grant_fail))
            }
        }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onAttach ${getName()}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onCreate ${getName()}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onCreateView ${getName()}")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onViewCreated ${getName()}")
    }

    override fun onStart() {
        super.onStart()
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onStart ${getName()}")
    }

    override fun onResume() {
        super.onResume()
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onResume ${getName()}")
    }

    override fun onPause() {
        super.onPause()
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onPause ${getName()}")
    }

    override fun onStop() {
        super.onStop()
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onStop ${getName()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onDestroyView ${getName()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onDestroy ${getName()}")
    }

    override fun onDetach() {
        super.onDetach()
        MLog.d(LogTag.LIFECYCLE_FRAGMENT, "onDetach ${getName()}")
    }

    private fun getName(): String {
        val sb: StringBuilder = StringBuilder(128)
        val cls: Class<*> = javaClass
        sb.append(cls.getSimpleName())
        sb.append("{")
        sb.append(Integer.toHexString(System.identityHashCode(this)))
        sb.append("}")
        return sb.toString()
    }

    /**
     * 检查存储权限，如果没有则申请。
     *
     * @param block 获得权限后的回调
     * @receiver
     */
    fun checkStoragePermission(block: () -> Unit) {
        this.block = block
        if (hasStoragePermission()) {
            block.invoke()
        } else {
            requestStoragePermission()
        }
    }

    private fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+
            Environment.isExternalStorageManager()
        } else {
            // Android 10 及以下
            val readPermission = ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            val writePermission = ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            readPermission && writePermission
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            showManageAllFilePermissionDialog()
        } else {
            val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (showRationale) {
                PermissionX.init(requireActivity())
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            this.block?.invoke()
                        } else {
                            MToast.show(
                                requireContext(),
                                getString(R.string.helper_permission_grant_fail)
                            )
                        }
                    }
            } else {
                if (!StorageUtil.getAlreadyRequestPermission()) {
                    PermissionX.init(requireActivity())
                        .permissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        .request { allGranted, grantedList, deniedList ->
                            if (allGranted) {
                                this.block?.invoke()
                            } else {
                                MToast.show(
                                    requireContext(),
                                    getString(R.string.helper_permission_grant_fail)
                                )
                            }
                        }
                } else {
                    showStoragePermissionDialog()
                }
            }
            StorageUtil.saveAlreadyRequestPermission()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${requireContext().packageName}")
        }
        settingsLauncher.launch(intent)
    }

    private fun showManageAllFilePermissionDialog() {
        PermissionHintDialog.newInstance(
            title = getString(R.string.helper_manage_all_title),
            content = getString(R.string.helper_manage_all_content)
        ).apply {
            setOnDialogClickListener(object : PermissionHintDialog.OnDialogClickListener {
                @RequiresApi(Build.VERSION_CODES.R)
                override fun onConfirmClick() {
                    try {
                        // 跳转到设置页面，让用户手动授予
                        val intent =
                            Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                                data = Uri.parse("package:${requireContext().packageName}")
                            }
                        launcherAllFile.launch(intent)
                    } catch (e: Exception) {
                        // 部分国产 ROM 可能会出错，回退到通用设置页面
                        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                        launcherAllFile.launch(intent)
                    }
                }

            })
        }.show(childFragmentManager, "ManageAllFilePermissionDialog")
    }

    private fun showStoragePermissionDialog() {
        PermissionHintDialog.newInstance(
            title = getString(R.string.helper_storage_title),
            content = getString(R.string.helper_storage_content)
        ).apply {
            setOnDialogClickListener(object : PermissionHintDialog.OnDialogClickListener {
                override fun onConfirmClick() {
                    openAppSettings()
                }
            })
        }.show(childFragmentManager, "StoragePermissionDialog")
    }

}