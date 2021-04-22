
package com.meetfriend.app.responseclasses.homeposts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class User implements Parcelable {

    @SerializedName("firstName")
    private String mFirstName;
    @SerializedName("id")
    private Integer mId;
    @SerializedName("lastName")
    private String mLastName;
    @SerializedName("profile_photo")
    private String mProfilePhoto;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @SerializedName("gender")
    private String gender;


    public String getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        this.cover_photo = cover_photo;
    }

    @SerializedName("cover_phot")
    private String cover_photo;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFirstName);
        dest.writeValue(this.mId);
        dest.writeString(this.mLastName);
        dest.writeString(this.mProfilePhoto);
        dest.writeString(this.cover_photo);
        dest.writeString(this.gender);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.mFirstName = in.readString();
        this.mId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mLastName = in.readString();
        this.mProfilePhoto = in.readString();
        this.cover_photo = in.readString();
        this.gender = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
