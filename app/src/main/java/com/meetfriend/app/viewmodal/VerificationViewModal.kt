package com.meetfriend.app.viewmodal

import androidx.lifecycle.MutableLiveData
import com.meetfriend.app.base.BaseViewModal
import com.meetfriend.app.repositories.ApiRepository
import com.meetfriend.app.responseclasses.CommonResponseClass
import com.meetfriend.app.responseclasses.LoginResponseClass

class VerificationViewModal:BaseViewModal() {

    var sendOtpResponse = MutableLiveData<CommonResponseClass>()
    fun sendOtp(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.sendOtp({
            sendOtpResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var verifyOtpResponse = MutableLiveData<CommonResponseClass>()
    fun verifyOtp(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.verifyOtp({
            verifyOtpResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var resendOtpResponse = MutableLiveData<CommonResponseClass>()
    fun resendOtp(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.verifyOtp({
            verifyOtpResponse.value = it
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