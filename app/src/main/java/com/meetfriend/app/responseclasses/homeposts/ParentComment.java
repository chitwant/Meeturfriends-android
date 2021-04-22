
package com.meetfriend.app.responseclasses.homeposts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParentComment implements Parcelable {

    @SerializedName("content")
    private String mContent;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("id")
    private Integer mId;
    @SerializedName("parent_id")
    private Integer mParentId;
    @SerializedName("post_id")
    private Integer mPostId;
    @SerializedName("user")
    private User mUser;
    @SerializedName("child_comments")
    private List<ChildComments> child_comments;
    @SerializedName("user_id")
    private Integer mUserId;

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Integer getParentId() {
        return mParentId;
    }

    public void setParentId(Integer parentId) {
        mParentId = parentId;
    }

    public Integer getPostId() {
        return mPostId;
    }

    public void setPostId(Integer postId) {
        mPostId = postId;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public void setUserId(Integer userId) {
        mUserId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mContent);
        dest.writeString(this.mCreatedAt);
        dest.writeValue(this.mId);
        dest.writeValue(this.mParentId);
        dest.writeValue(this.mPostId);
        dest.writeParcelable(this.mUser, flags);
        dest.writeValue(this.mUserId);
    }

    public ParentComment() {
    }

    protected ParentComment(Parcel in) {
        this.mContent = in.readString();
        this.mCreatedAt = in.readString();
        this.mId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mParentId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mPostId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mUser = in.readParcelable(User.class.getClassLoader());
        this.mUserId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ParentComment> CREATOR = new Parcelable.Creator<ParentComment>() {
        @Override
        public ParentComment createFromParcel(Parcel source) {
            return new ParentComment(source);
        }

        @Override
        public ParentComment[] newArray(int size) {
            return new ParentComment[size];
        }
    };


    public List<ChildComments> getChild_comments() {
        return child_comments;
    }

    public void setChild_comments(List<ChildComments> child_comments) {
        this.child_comments = child_comments;
    }
}
