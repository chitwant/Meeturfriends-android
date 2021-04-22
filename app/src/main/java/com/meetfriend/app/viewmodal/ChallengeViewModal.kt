package com.meetfriend.app.viewmodal

import androidx.lifecycle.MutableLiveData
import com.meetfriend.app.base.BaseViewModal
import com.meetfriend.app.responseclasses.challenge.countryResponseClass
import com.meetfriend.app.repositories.ApiRepository
import com.meetfriend.app.responseclasses.ChallengeDetailDataModel
import com.meetfriend.app.responseclasses.CommonResponseClass
import com.meetfriend.app.responseclasses.LikedUserDataModel
import com.meetfriend.app.responseclasses.challenge.mychallenge.MyChallengeResponseClass
import com.meetfriend.app.responseclasses.challenge.winner.WinnerResponseClass

class ChallengeViewModal : BaseViewModal() {
    var countryResponse = MutableLiveData<countryResponseClass>()
    fun country() {
        isLoading.value = true
        ApiRepository.fetchCountry({
            countryResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }

    var stateResponse = MutableLiveData<countryResponseClass>()
    fun state(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.fetchState({
            stateResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var cityResponse = MutableLiveData<countryResponseClass>()
    fun city(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.fetchCity({
            cityResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var winnerChallengeResponse = MutableLiveData<WinnerResponseClass>()
    fun fetchWinner(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.fetchWinner({
            winnerChallengeResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var myChallengeResponse = MutableLiveData<MyChallengeResponseClass>()
    fun fetchMyChallenge(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.fetchMyChallenge({
            myChallengeResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var challengeDetailsReponse = MutableLiveData<ChallengeDetailDataModel>()
    fun fetchChalllengeDetails(requestModel: HashMap<String, String>) {
        isLoading.value = true
        ApiRepository.fetchChallengeDetails({
            challengeDetailsReponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var challengeLikedlistReponse = MutableLiveData<LikedUserDataModel>()
    fun likedListChalenge(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.challengeLikeedList({
            challengeLikedlistReponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }
    var challengeLikeUnLikeReponse = MutableLiveData<CommonResponseClass>()
    fun likeUnlikeChalenge(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.challengeLikeUnlike({
            challengeLikeUnLikeReponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }
    var challengeViewReponse = MutableLiveData<CommonResponseClass>()
    fun challengeView(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.challengeView({
            challengeLikeUnLikeReponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var challengeAcceptPostReponse = MutableLiveData<MyChallengeResponseClass>()
    fun challengeAcceptPost(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.challengeAcceptPost({
            challengeAcceptPostReponse.value = it
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