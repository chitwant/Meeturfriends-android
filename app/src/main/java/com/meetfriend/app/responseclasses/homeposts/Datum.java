
package com.meetfriend.app.responseclasses.homeposts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Datum implements Parcelable {

    @SerializedName("content")
    private String mContent="";
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("id")
    private Integer mId;
    @SerializedName("is_liked_count")
    private Integer mIsLikedCount;
    @SerializedName("is_shared")
    private Integer mIsShared;
    @SerializedName("post_comments")
    private ArrayList<Comment> mPostComments;
    @SerializedName("post_likes_count")
    private Integer mPostLikesCount;
    @SerializedName("post_media")
    private ArrayList<PostMedium> mPostMedia;
    @SerializedName("shared_post")
    private SharedUser mSharedUser;
    @SerializedName("shared_user_id")
    private Integer mSharedUserId;
    @SerializedName("user")
    private User mUser;
    @SerializedName("user_id")
    private Integer mUserId;

    public ArrayList<ParentComment> getPost_likes() {
        return post_likes;
    }

    public void setPost_likes(ArrayList<ParentComment> post_likes) {
        this.post_likes = post_likes;
    }

    @SerializedName("post_likes")
    private ArrayList<ParentComment> post_likes;


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @SerializedName("location")
    private String location;

    public ArrayList<TaggedUser> getTagged_users_list() {
        return tagged_users_list;
    }

    public void setTagged_users_list(ArrayList<TaggedUser> tagged_users_list) {
        this.tagged_users_list = tagged_users_list;
    }

    @SerializedName("tagged_users")
    private ArrayList<TaggedUser> tagged_users_list;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    private String type;

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    @SerializedName("privacy")
    private Integer privacy;

    public Integer getNo_of_shared_count() {
        return no_of_shared_count;
    }

    public void setNo_of_shared_count(Integer no_of_shared_count) {
        this.no_of_shared_count = no_of_shared_count;
    }

    @SerializedName("no_of_shared_count")
    private Integer no_of_shared_count;


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

    public Integer getIsLikedCount() {
        return mIsLikedCount;
    }

    public void setIsLikedCount(Integer isLikedCount) {
        mIsLikedCount = isLikedCount;
    }

    public Integer getIsShared() {
        return mIsShared;
    }

    public void setIsShared(Integer isShared) {
        mIsShared = isShared;
    }

    public ArrayList<Comment> getPostComments() {
        return mPostComments;
    }

    public void setPostComments(ArrayList<Comment> postComments) {
        mPostComments = postComments;
    }

    public Integer getPostLikesCount() {
        return mPostLikesCount;
    }

    public void setPostLikesCount(Integer postLikesCount) {
        mPostLikesCount = postLikesCount;
    }

    public ArrayList<PostMedium> getPostMedia() {
        return mPostMedia;
    }

    public void setPostMedia(ArrayList<PostMedium> postMedia) {
        mPostMedia = postMedia;
    }

    public SharedUser getSharedUser() {
        return mSharedUser;
    }

    public void setSharedUser(SharedUser sharedUser) {
        mSharedUser = sharedUser;
    }

    public Integer getSharedUserId() {
        return mSharedUserId;
    }

    public void setSharedUserId(Integer sharedUserId) {
        mSharedUserId = sharedUserId;
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
        dest.writeValue(this.mIsLikedCount);
        dest.writeValue(this.mIsShared);
        dest.writeList(this.mPostComments);
        dest.writeValue(this.mPostLikesCount);
        dest.writeList(this.mPostMedia);
        dest.writeParcelable(this.mSharedUser, flags);
        dest.writeValue(this.mSharedUserId);
        dest.writeParcelable(this.mUser, flags);
        dest.writeValue(this.mUserId);
        dest.writeValue(this.privacy);
        dest.writeValue(this.no_of_shared_count);
        dest.writeValue(this.type);
        dest.writeList(this.tagged_users_list);
        dest.writeString(this.location);
        dest.writeList(this.post_likes);
    }

    public Datum() {
    }

    protected Datum(Parcel in) {
        this.mContent = in.readString();
        this.mCreatedAt = in.readString();
        this.mId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mIsLikedCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mIsShared = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mPostComments = new ArrayList<Comment>();
        in.readList(this.mPostComments, Comment.class.getClassLoader());
        this.mPostLikesCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mPostMedia = new ArrayList<PostMedium>();
        in.readList(this.mPostMedia, PostMedium.class.getClassLoader());
        this.mSharedUser = in.readParcelable(SharedUser.class.getClassLoader());
        this.mSharedUserId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mUser = in.readParcelable(User.class.getClassLoader());
        this.mUserId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.privacy = (Integer) in.readValue(Integer.class.getClassLoader());
        this.no_of_shared_count = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readString();
        this.tagged_users_list = new ArrayList<TaggedUser>();
        in.readList(this.tagged_users_list, TaggedUser.class.getClassLoader());
        this.location = in.readString();
        this.post_likes = new ArrayList<ParentComment>();
        in.readList(this.post_likes, ParentComment.class.getClassLoader());
    }

    public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel source) {
            return new Datum(source);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}
