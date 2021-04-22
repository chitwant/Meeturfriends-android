
package com.meetfriend.app.responseclasses.savedposts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SavedPostsResponseClass {

    @SerializedName("base_url")
    private String baseUrl;
    @SerializedName("media_url")
    private String mediaUrl;
    @Expose
    private String message;
    @Expose
    private Result result;
    @Expose
    private Boolean status;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
