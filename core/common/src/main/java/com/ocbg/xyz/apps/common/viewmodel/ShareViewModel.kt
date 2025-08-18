package com.ocbg.xyz.apps.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ocbg.xyz.apps.common.base.BaseViewModel

/**
 * 共享 ViewModel
 * User: Tom
 * Date: 2024/10/10 18:57
 */
class ShareViewModel : BaseViewModel() {

    // 点击主页的哪个tab项
    private val _clickHomeTab = MutableLiveData<Int?>()
    val clickHomeTab: LiveData<Int?> get() = _clickHomeTab

    /**
     * 切换科目
     *
     * @param tab
     */
    fun notifyHomeTabChanged(tab: Int?) {
        _clickHomeTab.value = tab
    }

}