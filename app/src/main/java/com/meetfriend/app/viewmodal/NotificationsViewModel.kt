package com.meetfriend.app.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meetfriend.app.base.BaseViewModal
import com.meetfriend.app.repositories.ApiRepository
import com.meetfriend.app.responseclasses.CommonResponseClass
import com.meetfriend.app.responseclasses.notification.NotificationResponseClass
import com.meetfriend.app.responseclasses.settings.SecurityManagmentResponse

class NotificationsViewModel : BaseViewModal() {
    var notifcationResponse = MutableLiveData<NotificationResponseClass>()
    fun fetchNotification(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.fetchNotification({
            notifcationResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var notifcationDeleteResponse = MutableLiveData<CommonResponseClass>()
    fun deleteNotification(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.deleteNotification({
            notifcationDeleteResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }
    var notifcationDeleteAllResponse = MutableLiveData<CommonResponseClass>()
    fun deleteAllNotification() {
        isLoading.value = true
        ApiRepository.deleteAllNotification({
            notifcationDeleteAllResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }
    var notifcationCountResponse = MutableLiveData<CommonResponseClass>()
    fun countNotification() {
        isLoading.value = true
        ApiRepository.notificationCount({
            notifcationCountResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }
    var notifcationReadAllResponse = MutableLiveData<CommonResponseClass>()
    fun readAllNotification() {
        ApiRepository.notificationReadAll({

        }, {

        }, {

        })
    }
}