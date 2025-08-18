package com.ocbg.xyz.apps.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import com.drake.spannable.addSpan
import com.drake.spannable.setSpan
import com.drake.spannable.span.ColorSpan
import com.ocbg.xyz.apps.common.util.MFileUtil
import com.ocbg.xyz.apps.ui.R
import com.ocbg.xyz.apps.ui.base.BaseUiDialogFragment
import com.ocbg.xyz.apps.ui.databinding.UiDialogConfirmBinding

/**
 * 二次确认 Dialog
 * User: Tom
 * Date: 2025/4/28 11:55
 */
class ConfirmDialog : BaseUiDialogFragment<UiDialogConfirmBinding>() {

    private var listener: OnDialogClickListener? = null

    companion object {
        private const val ARG_TITLE = "argTitle"
        private const val ARG_TEXT = "argText"
        private const val ARG_CANCEL_TEXT = "argCancelText"
        private const val ARG_OK_TEXT = "argOkText"
        private const val ARG_EXIT = "argExit"
        private const val ARG_DELETE = "argDelete"
        private const val ARG_NUMBER = "argNumber"
        private const val ARG_SIZE = "argSize"

        /**
         * 确认弹窗
         */
        fun newInstance(
            title: String?,
            text: String?,
            cancelText: String?,
            okText: String?
        ): ConfirmDialog {
            return ConfirmDialog().apply {
                isCancelable = false
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_TEXT, text)
                    putString(ARG_CANCEL_TEXT, cancelText)
                    putString(ARG_OK_TEXT, okText)
                }
            }
        }

        /**
         * 确认删除弹窗
         */
        fun newInstanceDelete(
            title: String? = null,
            text: String? = null,
            cancelText: String? = null,
            okText: String? = null,
            number: Int = 0
        ): ConfirmDialog {
            return newInstance(title, text, cancelText, okText).apply {
                arguments?.putBoolean(ARG_DELETE, true)
                arguments?.putInt(ARG_NUMBER, number)
            }
        }

        /**
         * 确认退出弹窗
         */
        fun newInstanceExit(
            title: String? = null,
            text: String? = null,
            cancelText: String? = null,
            okText: String? = null,
            size: Long = 0L
        ): ConfirmDialog {
            return newInstance(title, text, cancelText, okText).apply {
                arguments?.putBoolean(ARG_EXIT, true)
                arguments?.putLong(ARG_SIZE, size)
            }
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): UiDialogConfirmBinding {
        return UiDialogConfirmBinding.inflate(layoutInflater, container, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        // @formatter:off
        val isExit = arguments?.getBoolean(ARG_EXIT, false) ?: false
        if (isExit){
            val size = arguments?.getLong(ARG_SIZE) ?: 0L
            val fileSizeStr = MFileUtil.formatFileSize(size)
            val textStr = if (size <= 0 || fileSizeStr == null) {
                getString(R.string.ui_confirm_exit_text)
            } else {
                fileSizeStr
                    .setSpan(ColorSpan(getColor(requireContext(),R.color.ui_secondary_red)))
                    .addSpan(" ")
                    .addSpan(getString(R.string.ui_confirm_exit_text_1))
            }

            val title = arguments?.getString(ARG_TITLE) ?: getString(R.string.ui_confirm_exit_title)
            val text = arguments?.getString(ARG_TEXT) ?: textStr
            val cancel = arguments?.getString(ARG_CANCEL_TEXT) ?: getString(R.string.ui_confirm_exit_cancel)
            val ok = arguments?.getString(ARG_CANCEL_TEXT) ?: getString(R.string.ui_confirm_exit_ok)

            binding.ivIcon.setImageResource(R.drawable.ui_ic_dialog_exit)
            binding.tvTitle.text = title
            binding.tvText.text = text
            binding.btnCancel.text = cancel
            binding.btnOk.text = ok
        } else {
            val isWarn = arguments?.getBoolean(ARG_DELETE, false) ?: false
            if (isWarn) {
                val number = arguments?.getInt(ARG_NUMBER) ?: 0
                val textStr = if (number <= 0) {
                    getString(R.string.ui_confirm_delete_text)
                } else {
                    getString(R.string.ui_confirm_delete_text_1, number)
                        .setSpan(ColorSpan(getColor(requireContext(),R.color.ui_secondary_red)))
                        .addSpan(" ")
                        .addSpan(getString(R.string.ui_confirm_delete_text_2))
                }

                val title = arguments?.getString(ARG_TITLE) ?: getString(R.string.ui_confirm_delete_title)
                val text = arguments?.getString(ARG_TEXT) ?: textStr
                val cancel = arguments?.getString(ARG_CANCEL_TEXT) ?: getString(R.string.ui_confirm_delete_cancel)
                val ok = arguments?.getString(ARG_CANCEL_TEXT) ?: getString(R.string.ui_confirm_delete_ok)

                binding.ivIcon.setImageResource(R.drawable.ui_ic_dialog_delete)
                binding.tvTitle.text = title
                binding.tvText.text = text
                binding.btnCancel.text = cancel
                binding.btnOk.text = ok

                binding.btnOk.setBackgroundResource(R.drawable.ui_bg_btn_warn)
                binding.btnCancel.setTextColor(resources.getColor(R.color.ui_text_primary,null))
            } else {
                // 无需操作
            }
        }
        // @formatter:on
    }

    override fun initListener() {
        super.initListener()
        binding.btnCancel.setOnClickListener {
            dismiss()
            listener?.onCancel()
        }

        binding.btnOk.setOnClickListener {
            dismiss()
            listener?.onOk()
        }
    }

    interface OnDialogClickListener {
        fun onCancel()
        fun onOk()
    }

    fun setOnDialogClickListener(listener: OnDialogClickListener?) {
        this.listener = listener
    }

}