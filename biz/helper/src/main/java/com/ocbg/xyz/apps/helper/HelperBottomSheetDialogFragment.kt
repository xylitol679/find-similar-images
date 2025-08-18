package com.ocbg.xyz.apps.helper

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.ocbg.xyz.apps.common.util.MLog
import com.ocbg.xyz.apps.data.constant.LogTag
import com.ocbg.xyz.apps.ui.base.BaseUiBottomSheetDialogFragment

/**
 * 助手Fragment
 * User: Tom
 * Date: 2024/3/14 16:38
 */
abstract class HelperBottomSheetDialogFragment<T : ViewBinding> :
    BaseUiBottomSheetDialogFragment<T>() {

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

}