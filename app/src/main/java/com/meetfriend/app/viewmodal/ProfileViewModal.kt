package com.meetfriend.app.viewmodal

import androidx.lifecycle.MutableLiveData
import com.meetfriend.app.base.BaseViewModal
import com.meetfriend.app.repositories.ApiRepository
import com.meetfriend.app.responseclasses.CommonResponseClass
import com.meetfriend.app.responseclasses.homeposts.HomePostsResponseClass
import com.meetfriend.app.responseclasses.photos.UserPhotosResponse
import com.meetfriend.app.responseclasses.viewprofile.ViewProfileResponseClassNew

class ProfileViewModal :BaseViewModal() {
    var viewProfileResponse = MutableLiveData<ViewProfileResponseClassNew>()
    fun viewProfile(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.viewProfile({
            viewProfileResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var editPostResponse = MutableLiveData<CommonResponseClass>()
    fun editPost(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.editPost({
            editPostResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }
    var userPhotosResponse = MutableLiveData<UserPhotosResponse>()
    fun userPhotos(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.userPhotos({
            userPhotosResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var userPostsResponse = MutableLiveData<HomePostsResponseClass>()
    fun userPosts(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.userPosts({
            userPostsResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var deletePostResponse = MutableLiveData<CommonResponseClass>()
    fun deletePost(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.deletePost({
            deletePostResponse.value = it
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