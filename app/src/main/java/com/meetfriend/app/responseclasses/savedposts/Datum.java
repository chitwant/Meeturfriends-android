
package com.meetfriend.app.responseclasses.savedposts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Datum {

    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private Long id;
    @Expose
    private Post post;
    @SerializedName("post_id")
    private Long postId;
    @SerializedName("user_id")
    private Long userId;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
