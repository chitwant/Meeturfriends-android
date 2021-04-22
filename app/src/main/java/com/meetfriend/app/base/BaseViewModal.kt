package com.meetfriend.app.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModal:ViewModel() {
    var apiError = MutableLiveData<String>()
    var onFailure = MutableLiveData<Throwable>()
    var badRequest = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()
    var isPullToRefreshLoading = MutableLiveData<Boolean>()
    var isRemove = MutableLiveData<Boolean>()
}