package com.meetfriend.app.repositories

import android.text.Html
import com.meetfriend.app.modals.friendsuggestion.SuggestionResponseClass
import com.meetfriend.app.network.ApiHelper
import com.meetfriend.app.responseclasses.*
import com.meetfriend.app.responseclasses.blocked.BlockedUsersResponseClass
import com.meetfriend.app.responseclasses.challenge.countryResponseClass
import com.meetfriend.app.responseclasses.challenge.mychallenge.MyChallengeResponseClass
import com.meetfriend.app.responseclasses.challenge.winner.WinnerResponseClass
import com.meetfriend.app.responseclasses.friendrequest.FriendRequestResponseClass
import com.meetfriend.app.responseclasses.friends.FriendsResponseClass
import com.meetfriend.app.responseclasses.homeposts.HomePostsResponseClass
import com.meetfriend.app.responseclasses.notification.NotificationResponseClass
import com.meetfriend.app.responseclasses.photos.UserPhotosResponse
import com.meetfriend.app.responseclasses.postcomment.PostCommentResponseClass
import com.meetfriend.app.responseclasses.savedposts.SavedPostsResponseClass
import com.meetfriend.app.responseclasses.searchuser.SearchUserResponseClass
import com.meetfriend.app.responseclasses.settings.SecurityManagmentResponse
import com.meetfriend.app.responseclasses.viewprofile.ViewProfileResponseClassNew
import com.meetfriend.app.utilclasses.AppConstants
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ApiRepository {
    private val webService = ApiHelper.createService()
    private val webServiceAfterLogin = ApiHelper.createAppService()
    fun login(
        successHandler: (RegisterResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webService.login(requestModel)
            .enqueue(object : Callback<RegisterResponseClass> {
                override fun onResponse(
                    call: Call<RegisterResponseClass>,
                    response: Response<RegisterResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<RegisterResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun forgotPassword(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webService.forgotPassword(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun sendOtp(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webService.sendOtp(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun verifyOtp(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webService.verifyOtp(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun register(
        successHandler: (RegisterResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webService.register(requestModel)
            .enqueue(object : Callback<RegisterResponseClass> {
                override fun onResponse(
                    call: Call<RegisterResponseClass>,
                    response: Response<RegisterResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<RegisterResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun editProfileInfo(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.editProfileInfo(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun friendSuggestions(
        successHandler: (SuggestionResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.friendSuggestions(requestModel)
            .enqueue(object : Callback<SuggestionResponseClass> {
                override fun onResponse(
                    call: Call<SuggestionResponseClass>,
                    response: Response<SuggestionResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<SuggestionResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun posts(
        successHandler: (HomePostsResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.posts(requestModel)
            .enqueue(object : Callback<HomePostsResponseClass> {
                override fun onResponse(
                    call: Call<HomePostsResponseClass>,
                    response: Response<HomePostsResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<HomePostsResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }
  fun removeToken(

    ) {
        webServiceAfterLogin.removeToken()
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {

                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                    }
                }

            })
    }

    fun addFriend(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.addFriend(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun resetPassword(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.resetPassword(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun editPost(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.editPost(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(call: Call<CommonResponseClass>, response: Response<CommonResponseClass>) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if(response.code()==400){
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun viewProfile(
        successHandler: (ViewProfileResponseClassNew) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.viewProfile(requestModel)
            .enqueue(object : Callback<ViewProfileResponseClassNew> {
                override fun onResponse(
                    call: Call<ViewProfileResponseClassNew>,
                    response: Response<ViewProfileResponseClassNew>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<ViewProfileResponseClassNew>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun userPhotos(
        successHandler: (UserPhotosResponse) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.userPhotos(requestModel)
            .enqueue(object : Callback<UserPhotosResponse> {
                override fun onResponse(
                    call: Call<UserPhotosResponse>,
                    response: Response<UserPhotosResponse>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<UserPhotosResponse>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun userPosts(
        successHandler: (HomePostsResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.userPosts(requestModel)
            .enqueue(object : Callback<HomePostsResponseClass> {
                override fun onResponse(
                    call: Call<HomePostsResponseClass>,
                    response: Response<HomePostsResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<HomePostsResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun postLikeUnlike(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.postLikeUnlike(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun challengeLikeUnlike(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.challengeLikeUnlike(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun challengeView(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.challengeView(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun challengeLikeedList(
        successHandler: (LikedUserDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.challengeLikeedList(requestModel)
            .enqueue(object : Callback<LikedUserDataModel> {
                override fun onResponse(
                    call: Call<LikedUserDataModel>,
                    response: Response<LikedUserDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<LikedUserDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun reportOrHidePost(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.reportOrHidePost(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun deletePost(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.deletePost(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun commentPost(
        successHandler: (PostCommentResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.commentPost(requestModel)
            .enqueue(object : Callback<PostCommentResponseClass> {
                override fun onResponse(
                    call: Call<PostCommentResponseClass>,
                    response: Response<PostCommentResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<PostCommentResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }


    fun postShare(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.postShare(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun friendRequests(
        successHandler: (FriendRequestResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.friendRequests(requestModel)
            .enqueue(object : Callback<FriendRequestResponseClass> {
                override fun onResponse(
                    call: Call<FriendRequestResponseClass>,
                    response: Response<FriendRequestResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<FriendRequestResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun acceptOrRejectRequest(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.acceptOrRejectRequest(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun friendsList(
        successHandler: (FriendsResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.friendsList(requestModel)
            .enqueue(object : Callback<FriendsResponseClass> {
                override fun onResponse(
                    call: Call<FriendsResponseClass>,
                    response: Response<FriendsResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<FriendsResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun blockedList(
        successHandler: (BlockedUsersResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.blockedList(requestModel)
            .enqueue(object : Callback<BlockedUsersResponseClass> {
                override fun onResponse(
                    call: Call<BlockedUsersResponseClass>,
                    response: Response<BlockedUsersResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<BlockedUsersResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun blockUnBlockPeople(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.blockUnBlockPeople(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })


    }

    fun unfriend(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.unfriend(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun acceptFriendRequest(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.acceptFriendRequest(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun rejectFriendRequest(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.rejectFriendRequest(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun savePost(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.savePost(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }


    fun savedPostListing(
        successHandler: (SavedPostsResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.savedPostListing(requestModel)
            .enqueue(object : Callback<SavedPostsResponseClass> {
                override fun onResponse(
                    call: Call<SavedPostsResponseClass>,
                    response: Response<SavedPostsResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<SavedPostsResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun deleteSavedPost(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.deleteSavedPost(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun replyComment(
        successHandler: (PostCommentResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.replyComment(requestModel)
            .enqueue(object : Callback<PostCommentResponseClass> {
                override fun onResponse(
                    call: Call<PostCommentResponseClass>,
                    response: Response<PostCommentResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<PostCommentResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun deleteComment(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.deleteComment(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun updateComment(
        successHandler: (PostCommentResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.updateComment(requestModel)
            .enqueue(object : Callback<PostCommentResponseClass> {
                override fun onResponse(
                    call: Call<PostCommentResponseClass>,
                    response: Response<PostCommentResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<PostCommentResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun searchUsers(
        successHandler: (SearchUserResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.searchUsers(requestModel)
            .enqueue(object : Callback<SearchUserResponseClass> {
                override fun onResponse(
                    call: Call<SearchUserResponseClass>,
                    response: Response<SearchUserResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<SearchUserResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun privacyPolicy(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.privacyPolicy()
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun termsConditions(
        successHandler: (Html) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.termsCondition()
            .enqueue(object : Callback<Html> {
                override fun onResponse(
                    call: Call<Html>,
                    response: Response<Html>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<Html>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun fetchCountry(
        successHandler: (countryResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.country()
            .enqueue(object : Callback<countryResponseClass> {
                override fun onResponse(
                    call: Call<countryResponseClass>,
                    response: Response<countryResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<countryResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun myEarning(
        successHandler: (MyEarningDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.myEarning()
            .enqueue(object : Callback<MyEarningDataModel> {
                override fun onResponse(
                    call: Call<MyEarningDataModel>,
                    response: Response<MyEarningDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<MyEarningDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun giftSendTransaction(
        successHandler: (GiftTransactionDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.giftSendTransaction()
            .enqueue(object : Callback<GiftTransactionDataModel> {
                override fun onResponse(
                    call: Call<GiftTransactionDataModel>,
                    response: Response<GiftTransactionDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<GiftTransactionDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun giftReceivedTransaction(
        successHandler: (GiftTransactionDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.giftReceivedTransaction()
            .enqueue(object : Callback<GiftTransactionDataModel> {
                override fun onResponse(
                    call: Call<GiftTransactionDataModel>,
                    response: Response<GiftTransactionDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<GiftTransactionDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun fetchState(
        successHandler: (countryResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.state(requestModel)
            .enqueue(object : Callback<countryResponseClass> {
                override fun onResponse(
                    call: Call<countryResponseClass>,
                    response: Response<countryResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<countryResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }

    fun fetchCity(
        successHandler: (countryResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.city(requestModel)
            .enqueue(object : Callback<countryResponseClass> {
                override fun onResponse(
                    call: Call<countryResponseClass>,
                    response: Response<countryResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<countryResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun fetchSecurity(
        successHandler: (SecurityManagmentResponse) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.securityManagement(requestModel)
            .enqueue(object : Callback<SecurityManagmentResponse> {
                override fun onResponse(
                    call: Call<SecurityManagmentResponse>,
                    response: Response<SecurityManagmentResponse>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<SecurityManagmentResponse>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }
    fun cancelFriendRequest(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.cancelFriendRequest(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(call: Call<CommonResponseClass>, response: Response<CommonResponseClass>) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if(response.code()==400){
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }


                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }

            })
    }
    fun deleteSecurity(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.deleteSecurity(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun fetchNotification(
        successHandler: (NotificationResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.fetchNotification(requestModel)
            .enqueue(object : Callback<NotificationResponseClass> {
                override fun onResponse(
                    call: Call<NotificationResponseClass>,
                    response: Response<NotificationResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<NotificationResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun deleteNotification(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.deleteNotification(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun deleteAllNotification(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.deleteAllNotification()
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun notificationCount(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.notificationCount()
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun coinPlanListing(
        successHandler: (CoinListingDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.coinPlanListing()
            .enqueue(object : Callback<CoinListingDataModel> {
                override fun onResponse(
                    call: Call<CoinListingDataModel>,
                    response: Response<CoinListingDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CoinListingDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun bankCountry(
        successHandler: (BankCountryDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.bankCountry()
            .enqueue(object : Callback<BankCountryDataModel> {
                override fun onResponse(
                    call: Call<BankCountryDataModel>,
                    response: Response<BankCountryDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<BankCountryDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun bankDetail(
        successHandler: (BankDetailDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.bankDetail()
            .enqueue(object : Callback<BankDetailDataModel> {
                override fun onResponse(
                    call: Call<BankDetailDataModel>,
                    response: Response<BankDetailDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<BankDetailDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }
  fun claimCoins(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.claimCoin()
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun notificationReadAll(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        webServiceAfterLogin.notificationMarkRead()
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun fetchMyChallenge(
        successHandler: (MyChallengeResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.fetchMyChallenges(requestModel)
            .enqueue(object : Callback<MyChallengeResponseClass> {
                override fun onResponse(
                    call: Call<MyChallengeResponseClass>,
                    response: Response<MyChallengeResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<MyChallengeResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun fetchChallengeDetails(
        successHandler: (ChallengeDetailDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, String>
    ) {
        webServiceAfterLogin.fetchChallengeDetail(requestModel)
            .enqueue(object : Callback<ChallengeDetailDataModel> {
                override fun onResponse(
                    call: Call<ChallengeDetailDataModel>,
                    response: Response<ChallengeDetailDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<ChallengeDetailDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun getGiftListing(
        successHandler: (GiftListingDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Object>
    ) {
        webServiceAfterLogin.giftList(requestModel)
            .enqueue(object : Callback<GiftListingDataModel> {
                override fun onResponse(
                    call: Call<GiftListingDataModel>,
                    response: Response<GiftListingDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<GiftListingDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }
 fun getbankByCountry(
        successHandler: (BankByCountryDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Object>
    ) {
        webServiceAfterLogin.getBankByCountry(requestModel)
            .enqueue(object : Callback<BankByCountryDataModel> {
                override fun onResponse(
                    call: Call<BankByCountryDataModel>,
                    response: Response<BankByCountryDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<BankByCountryDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun getTransactionHistory(
        successHandler: (TransactionHistoryDataModel) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Object>
    ) {
        webServiceAfterLogin.transactionHistory(requestModel)
            .enqueue(object : Callback<TransactionHistoryDataModel> {
                override fun onResponse(
                    call: Call<TransactionHistoryDataModel>,
                    response: Response<TransactionHistoryDataModel>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<TransactionHistoryDataModel>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun challengeAcceptPost(
        successHandler: (MyChallengeResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.challengeAcceptPost(requestModel)
            .enqueue(object : Callback<MyChallengeResponseClass> {
                override fun onResponse(
                    call: Call<MyChallengeResponseClass>,
                    response: Response<MyChallengeResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<MyChallengeResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun coinsPurchase(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.coinPurchase(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun sendGift(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.sendGiftPost(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }
 fun updateBank(
        successHandler: (CommonResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.updateBank(requestModel)
            .enqueue(object : Callback<CommonResponseClass> {
                override fun onResponse(
                    call: Call<CommonResponseClass>,
                    response: Response<CommonResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }

    fun fetchWinner(
        successHandler: (WinnerResponseClass) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        requestModel: HashMap<String, Any>
    ) {
        webServiceAfterLogin.fetchWinner(requestModel)
            .enqueue(object : Callback<WinnerResponseClass> {
                override fun onResponse(
                    call: Call<WinnerResponseClass>,
                    response: Response<WinnerResponseClass>
                ) {
                    response.body()?.let {
                        successHandler(it)
                    }
                    if (response.code() == 400) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        failureHandler(jsonObj.getString("message"))
                    }
                    if (response.code() == AppConstants.httpcodes.STATUS_API_VALIDATION_ERROR) {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }

                    } else {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                }

                override fun onFailure(call: Call<WinnerResponseClass>, t: Throwable) {
                    t.let {
                        onFailure(it)
                    }
                }
            })
    }
}

//http://127.0.0.1:8000/api/challenge/winner?page=1&per_page=15