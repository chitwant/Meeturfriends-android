package com.meetfriend.app.viewmodal

import androidx.lifecycle.MutableLiveData
import com.meetfriend.app.base.BaseViewModal
import com.meetfriend.app.repositories.ApiRepository
import com.meetfriend.app.responseclasses.CommonResponseClass
import com.meetfriend.app.responseclasses.blocked.BlockedUsersResponseClass
import com.meetfriend.app.responseclasses.challenge.countryResponseClass
import com.meetfriend.app.responseclasses.settings.SecurityManagmentResponse

class SettingViewModal : BaseViewModal() {
    var securityMngmntResponse = MutableLiveData<SecurityManagmentResponse>()
    fun fetchSecurity(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.fetchSecurity({
            securityMngmntResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var securityMngmntDeleteResponse = MutableLiveData<CommonResponseClass>()
    fun deleteSecurity(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.deleteSecurity({
            securityMngmntDeleteResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var blockListResponse = MutableLiveData<BlockedUsersResponseClass>()
    fun fetchBlockList(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.deleteSecurity({
            securityMngmntDeleteResponse.value = it
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