package com.meetfriend.app.responseclasses.homeposts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comment implements Parcelable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("post_id")
    private Integer post_id;

    @SerializedName("user_id")
    private Integer user_id;

    @SerializedName("content")
    private String content;


    @SerializedName("created_at")
    private String created_at;

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    @SerializedName("parent_id")
    private Integer parent_id;

    @SerializedName("user")
    private User user;


    @SerializedName("child_comments")
    private List<ChildComments> child_comments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.post_id);
        dest.writeValue(this.user_id);
        dest.writeString(this.content);
        dest.writeString(this.created_at);
        dest.writeValue(this.parent_id);
        dest.writeParcelable(this.user, flags);
//        dest.writeParcelable((Parcelable) this.child_comments,flags);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.post_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.content = in.readString();
        this.created_at = in.readString();
        this.parent_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
//        this.child_comments = in.readParcelable(ParentComment.class.getClassLoader());
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public List<ChildComments> getChild_comments() {
        return child_comments;
    }

    public void setChild_comments(List<ChildComments> child_comments) {
        this.child_comments = child_comments;
    }
}
