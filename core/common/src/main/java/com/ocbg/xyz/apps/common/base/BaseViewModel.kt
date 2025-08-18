package com.ocbg.xyz.apps.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 结合 LiveData 和 Result 封装了常用的方法
 * User: Tom
 * Date: 2024/1/22 09:26
 */
open class BaseViewModel : ViewModel() {

    @JvmName("setSuccessValueNullable")
    protected fun <T : Any> setSuccessValue(liveData: MutableLiveData<Result<T?>>, value: T?) {
        liveData.value = Result.success(value)
    }

    @JvmName("setFailureValueNullable")
    protected fun <T : Any> setFailureValue(liveData: MutableLiveData<Result<T?>>, msg: String?) {
        liveData.value = Result.failure(Throwable(msg))
    }

    @JvmName("setSuccessValueNonNull")
    protected fun <T : Any> setSuccessValue(liveData: MutableLiveData<Result<T>>, value: T) {
        liveData.value = Result.success(value)
    }

    @JvmName("setFailureValueNonNull")
    protected fun <T : Any> setFailureValue(liveData: MutableLiveData<Result<T>>, msg: String?) {
        liveData.value = Result.failure(Throwable(msg))
    }

    @JvmName("postSuccessValueNullable")
    protected fun <T : Any> postSuccessValue(liveData: MutableLiveData<Result<T?>>, value: T?) {
        liveData.postValue(Result.success(value))
    }

    @JvmName("postFailureValueNullable")
    protected fun <T : Any> postFailureValue(liveData: MutableLiveData<Result<T?>>, msg: String?) {
        liveData.postValue(Result.failure(Throwable(msg)))
    }

    @JvmName("postSuccessValueNonNull")
    protected fun <T : Any> postSuccessValue(liveData: MutableLiveData<Result<T>>, value: T) {
        liveData.postValue(Result.success(value))
    }

    @JvmName("postFailureValueNonNull")
    protected fun <T : Any> postFailureValue(liveData: MutableLiveData<Result<T>>, msg: String?) {
        liveData.postValue(Result.failure(Throwable(msg)))
    }

}