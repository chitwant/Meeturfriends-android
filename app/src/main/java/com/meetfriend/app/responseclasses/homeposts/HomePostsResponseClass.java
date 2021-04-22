
package com.meetfriend.app.responseclasses.homeposts;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class HomePostsResponseClass {

    @SerializedName("base_url")
    private String mBaseUrl;
    @SerializedName("media_url")
    private String mMediaUrl;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private Result mResult;
    @SerializedName("status")
    private Boolean mStatus;

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

}
