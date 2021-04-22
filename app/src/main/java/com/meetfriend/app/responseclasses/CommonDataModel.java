package com.meetfriend.app.responseclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonDataModel {

    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("result")
    @Expose
    private String result;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
