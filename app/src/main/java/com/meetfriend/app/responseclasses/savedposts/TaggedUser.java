
package com.meetfriend.app.responseclasses.savedposts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TaggedUser {

    @Expose
    private Long id;
    @SerializedName("post_id")
    private Long postId;
    @SerializedName("tagged_user_id")
    private Long taggedUserId;
    @Expose
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getTaggedUserId() {
        return taggedUserId;
    }

    public void setTaggedUserId(Long taggedUserId) {
        this.taggedUserId = taggedUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
