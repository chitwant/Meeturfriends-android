package com.meetfriend.app.viewmodal

import androidx.lifecycle.MutableLiveData
import com.meetfriend.app.base.BaseViewModal
import com.meetfriend.app.repositories.ApiRepository
import com.meetfriend.app.responseclasses.CommonResponseClass
import com.meetfriend.app.responseclasses.LoginResponseClass
import com.meetfriend.app.responseclasses.RegisterResponseClass

class RegisterViewModal : BaseViewModal() {

    var loginResponse = MutableLiveData<RegisterResponseClass>()
    fun login(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.login({
            loginResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var forgotPasswordResponse = MutableLiveData<CommonResponseClass>()
    fun forgotPassword(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.forgotPassword({
            forgotPasswordResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var registerResponse = MutableLiveData<RegisterResponseClass>()
    fun register(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.register({
            registerResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var editProfileInfoResponse = MutableLiveData<CommonResponseClass>()
    fun editProfileInfo(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.editProfileInfo({
            editProfileInfoResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }
}