
package com.meetfriend.app.responseclasses.homeposts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TaggedUser implements Parcelable {

    @SerializedName("id")
    private Integer mId;
    @SerializedName("post_id")
    private Integer mPostId;
    @SerializedName("tagged_user_id")
    private Integer mTaggedUserId;
    @SerializedName("user")
    private User mUser;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Integer getPostId() {
        return mPostId;
    }

    public void setPostId(Integer postId) {
        mPostId = postId;
    }

    public Integer getTaggedUserId() {
        return mTaggedUserId;
    }

    public void setTaggedUserId(Integer taggedUserId) {
        mTaggedUserId = taggedUserId;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeValue(this.mPostId);
        dest.writeValue(this.mTaggedUserId);
        dest.writeParcelable(this.mUser, flags);
    }

    public TaggedUser() {
    }

    protected TaggedUser(Parcel in) {
        this.mId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mPostId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mTaggedUserId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<TaggedUser> CREATOR = new Parcelable.Creator<TaggedUser>() {
        @Override
        public TaggedUser createFromParcel(Parcel source) {
            return new TaggedUser(source);
        }

        @Override
        public TaggedUser[] newArray(int size) {
            return new TaggedUser[size];
        }
    };
}
