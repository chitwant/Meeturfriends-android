package com.meetfriend.app.network


import android.text.Html
import com.meetfriend.app.modals.friendsuggestion.SuggestionResponseClass
import com.meetfriend.app.responseclasses.*
import com.meetfriend.app.responseclasses.addpost.AddPostResponse
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 *  All web services are declared here
 */
@JvmSuppressWildcards
interface WebService {

    @FormUrlEncoded
    @POST("api/auth/login")
    fun login(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<RegisterResponseClass>

    @FormUrlEncoded
    @POST("api/auth/register")
    fun register(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<RegisterResponseClass>

    @FormUrlEncoded
    @POST("api/auth/forgot-password")
    fun forgotPassword(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/auth/send-otp")
    fun sendOtp(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/auth/verify-otp")
    fun verifyOtp(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/auth/edit-profile")
    fun editProfileInfo(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @GET("api/home/suggestion")
    fun friendSuggestions(@QueryMap data: Map<String, Any>): Call<SuggestionResponseClass>

    @GET("api/home/posts")
    fun posts(@QueryMap data: Map<String, Any>): Call<HomePostsResponseClass>

    @GET("api/common/user/empty-duplicate-device-token")
    fun removeToken(): Call<CommonResponseClass>

    @POST("api/post/create")
    fun uploadMultiFile(@Body file: RequestBody?): Call<ResponseBody>?


//    @FormUrlEncoded
//    @POST("post/edit")
//    fun editPost(
//        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
//    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/home/add-friend")
    fun addFriend(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @Multipart
    @POST("api/post/edit")
    fun editSharedPost(
        @Part("post_id") post_id: RequestBody?,
        @Part("content") content: RequestBody?
    ): Call<AddPostResponse?>?


    @FormUrlEncoded
    @POST("api/auth/reset-password")
    fun resetPassword(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/auth/view-profile")
    fun viewProfile(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<ViewProfileResponseClassNew>

    @FormUrlEncoded
    @POST("api/user/photos")
    fun userPhotos(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<UserPhotosResponse>

    @FormUrlEncoded
    @POST("api/user/posts")
    fun userPosts(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<HomePostsResponseClass>

    @POST("api/auth/upload-profile-photo")
    fun uploadProfilePicture(@Body file: RequestBody?): Call<UpdatePhotoResponse>?

    @Multipart
    @POST("api/auth/upload-profile-photo")
    fun uploadProfilePicture(@Part multipartTypedOutput: MultipartBody.Part?): Call<UpdatePhotoResponse>?

    @POST("api/auth/upload-cover-photo")
    fun uploadCoverPicture(@Body file: RequestBody?): Call<UpdatePhotoResponse>?


    @FormUrlEncoded
    @POST("api/post/like-dislike")
    fun postLikeUnlike(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/challenge/post/like-unlike")
    fun challengeLikeUnlike(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/challenge/post/view-by-user")
    fun challengeView(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @GET("api/challenge/post/liked-users-listing")
    fun challengeLikeedList(
        @QueryMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<LikedUserDataModel>

    @FormUrlEncoded
    @POST("api/post/report-or-hide")
    fun reportOrHidePost(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/post/delete")
    fun deletePost(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/post/comment")
    fun commentPost(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<PostCommentResponseClass>


    @Multipart
    @POST("api/post/edit")
    fun editPost(
        @Part("post_id") post_id: RequestBody?,
        @Part("privacy") privacy: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("content") content: RequestBody?,
        @Part("tagged_user_id") tagged_user_id: RequestBody?,
        @Part("deleted_media_ids") deleted_media_ids: RequestBody?,
        @Part multipartTypedOutput: Array<MultipartBody.Part?>?
    ): Call<AddPostResponse?>?


    @POST("post/edit")
    fun editPost(@Body file: RequestBody?): Call<ResponseBody>?

    @FormUrlEncoded
    @POST("api/post/edit")
    fun editPost(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/post/share")
    fun postShare(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @Multipart
    @POST("api/post/create")
    fun addPost(
        @Part("privacy") privacy: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("content") content: RequestBody?,
        @Part("tagged_user_id") tagged_user_id: RequestBody?,
        @Part multipartTypedOutput: Array<MultipartBody.Part?>?
    ): Call<AddPostResponse?>?


    @GET("api/friend/pending-list")
    fun friendRequests(@QueryMap data: Map<String, Any>): Call<FriendRequestResponseClass>


    @FormUrlEncoded
    @POST("api/friend/accept-declined-friend-request")
    fun acceptOrRejectRequest(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @GET("api/friend/accepted-list")
    fun friendsList(
        @QueryMap data: Map<String, Any>
    ): Call<FriendsResponseClass>


    @GET("api/friend/block-list")
    fun blockedList(
        @QueryMap data: Map<String, Any>
    ): Call<BlockedUsersResponseClass>

    @FormUrlEncoded
    @POST("api/friend/block-unblock-friend")
    fun blockUnBlockPeople(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/friend/unfriend")
    fun unfriend(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/friend/accept-friend-request")
    fun acceptFriendRequest(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/friend/declined-friend-request")
    fun rejectFriendRequest(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>


    @FormUrlEncoded
    @POST("api/post/save-post/create")
    fun savePost(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    /* @GET("post/save-post")
     fun savedPostListing(
         @QueryMap data: Map<String, Any>
     ): Call<HomePostsResponseClass>*/

    @GET("api/post/save-post/index")
    fun savedPostListing(@QueryMap data: Map<String, Any>): Call<SavedPostsResponseClass>

    @FormUrlEncoded
    @POST("api/post/save-post/delete")
    fun deleteSavedPost(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/post/comment/reply")
    fun replyComment(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<PostCommentResponseClass>


    @FormUrlEncoded
    @POST("api/post/comment/delete")
    fun deleteComment(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>


    @FormUrlEncoded
    @POST("api/post/comment/update")
    fun updateComment(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<PostCommentResponseClass>


    @GET("api/friend/search")
    fun searchUsers(@QueryMap data: Map<String, Any>): Call<SearchUserResponseClass>

    @Headers("Content-Type: text/html")
    @GET("privacy-policy")
    fun privacyPolicy(
    ): Call<CommonResponseClass>

    @Headers("Content-Type: text/html")
    @GET("terms-conditions")
    fun termsCondition(
    ): Call<Html>


    @GET("api/common/country")
    fun country(
    ): Call<countryResponseClass>

    @GET("api/common/state")
    fun state(
        @QueryMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<countryResponseClass>

    @GET("api/common/city")
    fun city(
        @QueryMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<countryResponseClass>

    @POST("api/challenge/create")
    fun uploadChallengeFile(@Body file: RequestBody?): Call<ResponseBody>?

    @POST("api/challenge/post/create")
    fun uploadChallengeAcceptFile(@Body file: RequestBody?): Call<ResponseBody>?

    @GET("api/security-management/index")
    fun securityManagement(@QueryMap data: Map<String, Any>): Call<SecurityManagmentResponse>


    @FormUrlEncoded
    @POST("api/home/cancel-add-friend")
    fun cancelFriendRequest(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/security-management/delete")
    fun deleteSecurity(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @GET("api/notifications/index")
    fun fetchNotification(@QueryMap data: Map<String, Any>): Call<NotificationResponseClass>

    @FormUrlEncoded
    @POST("api/notifications/delete")
    fun deleteNotification(
        @FieldMap data: HashMap<String, @JvmSuppressWildcards Any>
    ): Call<CommonResponseClass>

    @POST("api/notifications/delete-all")
    fun deleteAllNotification(): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/challenge/view")
    fun fetchChallengeDetail(@FieldMap data: Map<String, Any>): Call<ChallengeDetailDataModel>

    @FormUrlEncoded
    @POST("api/challenge/accepted-rejected")
    fun challengeAcceptPost(@FieldMap data: Map<String, Any>): Call<MyChallengeResponseClass>

    @GET("api/challenge/winner")
    fun fetchWinner(@QueryMap data: Map<String, Any>): Call<WinnerResponseClass>

    @GET("api/challenge/index")
    fun fetchMyChallenges(@QueryMap data: Map<String, Any>): Call<MyChallengeResponseClass>

// gift section api

    @GET("api/user/claim-coins")
    fun claimCoin(): Call<CommonResponseClass>

    @GET("api/user/get-bank-details")
    fun bankDetail(): Call<BankDetailDataModel>

    @GET("api/common/bank-countries")
    fun bankCountry(): Call<BankCountryDataModel>

    @GET("api/common/coins-plan")
    fun coinPlanListing(): Call<CoinListingDataModel>

    @GET("api/notifications/count-unread")
    fun notificationCount(): Call<CommonResponseClass>

    @GET("api/notifications/mark-all-read")
    fun notificationMarkRead(): Call<CommonResponseClass>

    @GET("api/coins/myearning")
    fun myEarning(): Call<MyEarningDataModel>

    @GET("api/coins/gift-send-transaction")
    fun giftSendTransaction(): Call<GiftTransactionDataModel>

    @GET("api/coins/gift-recieved-transaction")
    fun giftReceivedTransaction(): Call<GiftTransactionDataModel>

    @GET("api/gift/index")
    fun giftList(@QueryMap data: Map<String, Object>): Call<GiftListingDataModel>

    @GET("api/common/bank-details-by-country")
    fun getBankByCountry(@QueryMap data: Map<String, Object>): Call<BankByCountryDataModel>

    @GET("api/coins/transaction-history")
    fun transactionHistory(@QueryMap data: Map<String, Object>): Call<TransactionHistoryDataModel>

    @FormUrlEncoded
    @POST("api/coins/gift")
    fun sendGiftPost(@FieldMap data: Map<String, Any>): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/user/update-bank-details")
    fun updateBank(@FieldMap data: Map<String, Any>): Call<CommonResponseClass>

    @FormUrlEncoded
    @POST("api/coins/purchase")
    fun coinPurchase(@FieldMap data: Map<String, Any>): Call<CommonResponseClass>

}