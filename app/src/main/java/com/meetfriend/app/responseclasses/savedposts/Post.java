
package com.meetfriend.app.responseclasses.savedposts;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meetfriend.app.responseclasses.homeposts.Comment;
import com.meetfriend.app.responseclasses.homeposts.PostMedium;
import com.meetfriend.app.responseclasses.homeposts.SharedUser;

@SuppressWarnings("unused")
public class Post {

    @Expose
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private Integer id;
    @SerializedName("is_liked_count")
    private Integer isLikedCount;
    @SerializedName("is_shared")
    private Integer isShared;
    @Expose
    private String location;
    @SerializedName("no_of_shared_count")
    private Integer noOfSharedCount;
    @SerializedName("post_comments")
    private ArrayList<Comment> postComments;
    @SerializedName("post_likes_count")
    private Integer postLikesCount;
    @SerializedName("post_media")
    private ArrayList<PostMedium> postMedia;
    @Expose
    private Integer privacy;
    @SerializedName("shared_post")
    private SharedUser sharedPost;
    @SerializedName("shared_post_id")
    private Integer sharedPostId;
    @SerializedName("shared_user_id")
    private Integer sharedUserId;
    @SerializedName("tagged_users")
    private ArrayList<TaggedUser> taggedUsers;
    @Expose
    private String type;
    @Expose
    private User user;
    @SerializedName("user_id")
    private Integer userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsLikedCount() {
        return isLikedCount;
    }

    public void setIsLikedCount(Integer isLikedCount) {
        this.isLikedCount = isLikedCount;
    }

    public Integer getIsShared() {
        return isShared;
    }

    public void setIsShared(Integer isShared) {
        this.isShared = isShared;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getNoOfSharedCount() {
        return noOfSharedCount;
    }

    public void setNoOfSharedCount(Integer noOfSharedCount) {
        this.noOfSharedCount = noOfSharedCount;
    }

    public ArrayList<Comment> getPostComments() {
        return postComments;
    }

    public void setPostComments(ArrayList<Comment> postComments) {
        this.postComments = postComments;
    }

    public Integer getPostLikesCount() {
        return postLikesCount;
    }

    public void setPostLikesCount(Integer postLikesCount) {
        this.postLikesCount = postLikesCount;
    }

    public ArrayList<PostMedium> getPostMedia() {
        return postMedia;
    }

    public void setPostMedia(ArrayList<PostMedium> postMedia) {
        this.postMedia = postMedia;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public SharedUser getSharedPost() {
        return sharedPost;
    }

    public void setSharedPost(SharedUser sharedPost) {
        this.sharedPost = sharedPost;
    }

    public Integer getSharedPostId() {
        return sharedPostId;
    }

    public void setSharedPostId(Integer sharedPostId) {
        this.sharedPostId = sharedPostId;
    }

    public Integer getSharedUserId() {
        return sharedUserId;
    }

    public void setSharedUserId(Integer sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    public ArrayList<TaggedUser> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(ArrayList<TaggedUser> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
