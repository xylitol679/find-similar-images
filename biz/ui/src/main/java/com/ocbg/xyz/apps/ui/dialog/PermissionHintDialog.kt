package com.ocbg.xyz.apps.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ocbg.xyz.apps.ui.base.BaseUiBottomSheetDialogFragment
import com.ocbg.xyz.apps.ui.databinding.UiDialogPermissionHintBinding


class PermissionHintDialog :
    BaseUiBottomSheetDialogFragment<UiDialogPermissionHintBinding>() {

    private var listener: OnDialogClickListener? = null

    companion object {

        private const val ARG_TITLE = "arg_title"
        private const val ARG_CONTENT = "arg_content"

        fun newInstance(title: String, content: String): PermissionHintDialog {
            return PermissionHintDialog().apply {
                isCancelable = true
                arguments = bundleOf(
                    ARG_TITLE to title,
                    ARG_CONTENT to content
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.isDraggable = false
        dialog.behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        return dialog
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): UiDialogPermissionHintBinding {
        return UiDialogPermissionHintBinding.inflate(inflater, container, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.tvTitle.text = arguments?.getString(ARG_TITLE)
        binding.tvContent.text = arguments?.getString(ARG_CONTENT)
    }

    override fun initListener() {
        super.initListener()
        binding.btnConfirm.setOnClickListener {
            listener?.onConfirmClick()
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    interface OnDialogClickListener {
        fun onConfirmClick()
    }

    fun setOnDialogClickListener(listener: OnDialogClickListener?) {
        this.listener = listener
    }


}