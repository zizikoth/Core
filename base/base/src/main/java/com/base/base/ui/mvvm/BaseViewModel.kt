package com.base.base.ui.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.rxLifeScope
import com.base.base.entity.uistatus.UiStatus
import com.base.base.http.ApiCode
import com.base.base.http.ExceptionHandler
import com.blankj.utilcode.util.ToastUtils

/**
 * title:
 * describe:
 *
 * @author memo
 * @date 2020-12-18 10:49 PM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */
open class BaseViewModel : ViewModel() {

    /*** 是否是第一次加载 ***/
    private var isFirstLoad = false

    /*** 请求加载框 ***/
    val loadingEvent: MutableLiveData<Boolean> = MutableLiveData()

    /*** 页面展示状态 ***/
    val statusEvent: MutableLiveData<UiStatus> = MutableLiveData()

    fun <T> request(request: (suspend () -> T), onSuccess: ((data: T) -> Unit), onError: ((code: Int) -> Unit)) {
        rxLifeScope.launch(
            block = {
                onSuccess(request.invoke())
                isFirstLoad = false
                statusEvent.postValue(UiStatus(isFirstLoad, ApiCode.Success))
            },
            onError = {
                val error = ExceptionHandler.handleException(it)
                ToastUtils.showShort(error.message)
                statusEvent.postValue(UiStatus(isFirstLoad, error.code))
                onError(error.code)
            },
            onStart = {
                loadingEvent.postValue(true)
            },
            onFinally = {
                loadingEvent.postValue(false)

            })
    }

    fun requestNoCheck(request: suspend () -> Unit) {
        rxLifeScope.launch { request.invoke() }
    }

}