
package com.meetfriend.app.responseclasses.homeposts;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Link {

    @SerializedName("active")
    private Boolean mActive;
    @SerializedName("label")
    private String mLabel;
    @SerializedName("url")
    private Object mUrl;

    public Boolean getActive() {
        return mActive;
    }

    public void setActive(Boolean active) {
        mActive = active;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public Object getUrl() {
        return mUrl;
    }

    public void setUrl(Object url) {
        mUrl = url;
    }

}
