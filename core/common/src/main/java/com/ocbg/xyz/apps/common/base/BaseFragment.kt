package com.ocbg.xyz.apps.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ocbg.xyz.apps.common.util.MLog

/**
 * Fragment基类
 * User: Tom
 * Date: 2025/2/26 10:23
 */
abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MLog.d("BaseFragment", "${this}\tonCreate")
        initData(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MLog.d("BaseFragment", "${this}\tonViewCreated")
        initToolbar(savedInstanceState)
        initView(savedInstanceState)
        initListener()
        subscribeUi()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MLog.d("BaseFragment", "${this}\tonDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        MLog.d("BaseFragment", "${this}\tonDestroy")
    }

    protected open fun initData(savedInstanceState: Bundle?) {
    }

    abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T

    protected open fun initToolbar(savedInstanceState: Bundle?) {
    }

    protected open fun initView(savedInstanceState: Bundle?) {
    }

    protected open fun initListener() {
    }

    protected open fun subscribeUi() {
    }

    protected open fun loadData() {
    }

}