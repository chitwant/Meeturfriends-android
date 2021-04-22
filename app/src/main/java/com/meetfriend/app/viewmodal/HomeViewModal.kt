package com.meetfriend.app.viewmodal

import android.text.Html
import androidx.lifecycle.MutableLiveData
import com.meetfriend.app.base.BaseViewModal
import com.meetfriend.app.modals.friendsuggestion.SuggestionResponseClass
import com.meetfriend.app.repositories.ApiRepository
import com.meetfriend.app.responseclasses.CommonResponseClass
import com.meetfriend.app.responseclasses.blocked.BlockedUsersResponseClass
import com.meetfriend.app.responseclasses.friendrequest.FriendRequestResponseClass
import com.meetfriend.app.responseclasses.friends.FriendsResponseClass
import com.meetfriend.app.responseclasses.homeposts.HomePostsResponseClass
import com.meetfriend.app.responseclasses.postcomment.PostCommentResponseClass
import com.meetfriend.app.responseclasses.savedposts.SavedPostsResponseClass
import com.meetfriend.app.responseclasses.searchuser.SearchUserResponseClass

class HomeViewModal : BaseViewModal() {
    var friendSuggestionsResponse = MutableLiveData<SuggestionResponseClass>()
    fun friendSuggestions(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.friendSuggestions({
            friendSuggestionsResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var homePostsResponse = MutableLiveData<HomePostsResponseClass>()
    fun homePosts(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.posts({
            homePostsResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var removetokenResponse = MutableLiveData<CommonResponseClass>()
    fun removeToken() {
        isLoading.value = true
        ApiRepository.removeToken()
    }


    var cancelFriendRequestResponse = MutableLiveData<CommonResponseClass>()
    fun cancelFriendRequest(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.cancelFriendRequest({
            cancelFriendRequestResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var addFriendResponse = MutableLiveData<CommonResponseClass>()
    fun addFriend(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.addFriend({
            addFriendResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var resetPasswordResponse = MutableLiveData<CommonResponseClass>()
    fun resetPassword(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.resetPassword({
            resetPasswordResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var postLikeUnlikeResponse = MutableLiveData<CommonResponseClass>()
    fun postLikeUnlike(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.postLikeUnlike({
            postLikeUnlikeResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var reportOrHidePostResponse = MutableLiveData<CommonResponseClass>()
    fun reportOrHidePost(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.reportOrHidePost({
            reportOrHidePostResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var commentPostResponse = MutableLiveData<PostCommentResponseClass>()
    fun commentPost(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.commentPost({
            commentPostResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var postShareResponse = MutableLiveData<CommonResponseClass>()
    fun postShare(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.postShare({
            postShareResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var friendRequestsResponse = MutableLiveData<FriendRequestResponseClass>()
    fun friendRequests(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.friendRequests({
            friendRequestsResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var acceptOrRejectRequestResponse = MutableLiveData<CommonResponseClass>()
    fun acceptOrRejectRequest(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.acceptOrRejectRequest({
            acceptOrRejectRequestResponse.value = it
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


    var friendsListResponse = MutableLiveData<FriendsResponseClass>()
    fun friendsList(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.friendsList({
            friendsListResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var blockedListResponse = MutableLiveData<BlockedUsersResponseClass>()
    fun blockedList(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.blockedList({
            blockedListResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var blockUnBlockPeopleResponse = MutableLiveData<CommonResponseClass>()
    fun blockUnBlockPeople(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.blockUnBlockPeople({
            blockUnBlockPeopleResponse.value = it
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

    var unfriendResponse = MutableLiveData<CommonResponseClass>()
    fun unfriend(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.unfriend({
            unfriendResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var acceptFriendRequestResponse = MutableLiveData<CommonResponseClass>()
    fun acceptFriendRequest(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.acceptFriendRequest({
            acceptFriendRequestResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var rejectFriendRequestResponse = MutableLiveData<CommonResponseClass>()
    fun rejectFriendRequest(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.rejectFriendRequest({
            rejectFriendRequestResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var savePostRequestResponse = MutableLiveData<CommonResponseClass>()
    fun savePost(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.savePost({
            savePostRequestResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var savedPostListingResponse = MutableLiveData<SavedPostsResponseClass>()
    fun savedPostListing(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.savedPostListing({
            savedPostListingResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var deleteSavedPostResponse = MutableLiveData<CommonResponseClass>()
    fun deleteSavedPost(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.deleteSavedPost({
            deleteSavedPostResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var replyCommentResponse = MutableLiveData<PostCommentResponseClass>()
    fun replyComment(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.replyComment({
            replyCommentResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var deleteCommentResponse = MutableLiveData<CommonResponseClass>()
    fun deleteComment(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.deleteComment({
            deleteCommentResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var updateCommentResponse = MutableLiveData<PostCommentResponseClass>()
    fun updateComment(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.updateComment({
            updateCommentResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var searchUsersResponse = MutableLiveData<SearchUserResponseClass>()
    fun searchUsers(requestModel: HashMap<String, Any>) {
        isLoading.value = true
        ApiRepository.searchUsers({
            searchUsersResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        }, requestModel)
    }

    var privacyPolicyResponse = MutableLiveData<CommonResponseClass>()
    fun privacyPolicy() {
        isLoading.value = true
        ApiRepository.privacyPolicy({
            privacyPolicyResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }

    var termsConditionResponse = MutableLiveData<Html>()
    fun termsCondition() {
        isLoading.value = true
        ApiRepository.termsConditions({
            termsConditionResponse.value = it
            isLoading.value = false
        }, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }
}