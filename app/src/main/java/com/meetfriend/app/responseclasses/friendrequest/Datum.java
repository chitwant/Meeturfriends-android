
package com.meetfriend.app.responseclasses.friendrequest;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Datum {

    @SerializedName("block_status")
    private String mBlockStatus;
    @SerializedName("friend_id")
    private Integer mFriendId;
    @SerializedName("friend_status")
    private String mFriendStatus;
    @SerializedName("id")
    private Integer mId;
    @SerializedName("pending_user")
    private PendingUser mPendingUser;
    @SerializedName("request_status")
    private String mRequestStatus;
    @SerializedName("user_id")
    private Integer mUserId;

    public String getBlockStatus() {
        return mBlockStatus;
    }

    public void setBlockStatus(String blockStatus) {
        mBlockStatus = blockStatus;
    }

    public Integer getFriendId() {
        return mFriendId;
    }

    public void setFriendId(Integer friendId) {
        mFriendId = friendId;
    }

    public String getFriendStatus() {
        return mFriendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        mFriendStatus = friendStatus;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public PendingUser getPendingUser() {
        return mPendingUser;
    }

    public void setPendingUser(PendingUser pendingUser) {
        mPendingUser = pendingUser;
    }

    public String getRequestStatus() {
        return mRequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        mRequestStatus = requestStatus;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public void setUserId(Integer userId) {
        mUserId = userId;
    }

}
