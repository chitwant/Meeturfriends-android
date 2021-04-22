
package com.meetfriend.app.responseclasses.friendrequest;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PendingUser {

    @SerializedName("cover_photo")
    private String mCoverPhoto;
    @SerializedName("firstName")
    private String mFirstName;
    @SerializedName("id")
    private Long mId;
    @SerializedName("lastName")
    private String mLastName;
    @SerializedName("profile_photo")
    private String mProfilePhoto;

    public String getCoverPhoto() {
        return mCoverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        mCoverPhoto = coverPhoto;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getProfilePhoto() {
        return mProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        mProfilePhoto = profilePhoto;
    }

}
