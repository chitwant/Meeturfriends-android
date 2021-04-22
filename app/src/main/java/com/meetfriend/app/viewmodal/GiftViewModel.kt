package com.meetfriend.app.viewmodal

import androidx.lifecycle.MutableLiveData
import com.meetfriend.app.base.BaseViewModal
import com.meetfriend.app.repositories.ApiRepository
import com.meetfriend.app.responseclasses.*

class GiftViewModel : BaseViewModal() {
    var giftListReponse = MutableLiveData<GiftListingDataModel>()
    fun getgiftList(requestModel: HashMap<String, Object>) {
        isLoading.value = true
        ApiRepository.getGiftListing({
            giftListReponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var bankByCountryResponse = MutableLiveData<BankByCountryDataModel>()
    fun getBankByCountry(requestModel: HashMap<String, Object>) {
        isLoading.value = true
        ApiRepository.getbankByCountry({
            bankByCountryResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var transactioHistoryResponse = MutableLiveData<TransactionHistoryDataModel>()
    fun getTransactionHistory(requestModel: HashMap<String, Object>) {
        isLoading.value = true
        ApiRepository.getTransactionHistory({
            transactioHistoryResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var myEarningResponse = MutableLiveData<MyEarningDataModel>()
    fun myearning() {
        isLoading.value = true
        ApiRepository.myEarning({
            myEarningResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }

    var coinListResponse = MutableLiveData<CoinListingDataModel>()
    fun getCoinList() {
        isLoading.value = true
        ApiRepository.coinPlanListing({
            coinListResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }

    var bankCountryResponse = MutableLiveData<BankCountryDataModel>()
    fun getBankCountry() {
        isLoading.value = true
        ApiRepository.bankCountry({
            bankCountryResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }

    var bankDetailResponse = MutableLiveData<BankDetailDataModel>()
    fun getBankDetail() {
        isLoading.value = true
        ApiRepository.bankDetail({
            bankDetailResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }

    var claimCoinResponse = MutableLiveData<CommonResponseClass>()
    fun clainCoin() {
        isLoading.value = true
        ApiRepository.claimCoins({
            claimCoinResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }

    var giftsentResponse = MutableLiveData<GiftTransactionDataModel>()
    fun giftSent() {
        isLoading.value = true
        ApiRepository.giftSendTransaction({
            giftsentResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }

    var giftReceivedResponse = MutableLiveData<GiftTransactionDataModel>()
    fun giftReceived() {
        isLoading.value = true
        ApiRepository.giftReceivedTransaction({
            giftReceivedResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }


    var coinPurchaseResponse = MutableLiveData<CommonResponseClass>()
    fun coinPurchase(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.coinsPurchase({
            coinPurchaseResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var giftSendResponse = MutableLiveData<CommonResponseClass>()
    fun giftSent(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.sendGift({
            giftSendResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var updateBankResponse = MutableLiveData<CommonResponseClass>()
    fun updateBank(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.updateBank({
            updateBankResponse.value = it
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