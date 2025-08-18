package com.ocbg.xyz.apps.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.drake.statusbar.immersive

/**
 * Activity基类
 * User: Tom
 * Date: 2025/2/26 10:23
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isEdgeToEdge()) {
            immersive(darkMode = true)
//            enableEdgeToEdge()
        }
        initData(savedInstanceState)
        binding = createBinding(savedInstanceState)
        setContentView(binding.root)
        initToolbar(savedInstanceState)
        initView(savedInstanceState)
        initListener()
        subscribeUi()
        loadData()
    }

    protected open fun initData(savedInstanceState: Bundle?) {
    }

    abstract fun isEdgeToEdge(): Boolean

    abstract fun createBinding(savedInstanceState: Bundle?): T

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