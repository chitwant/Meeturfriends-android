
package com.meetfriend.app.responseclasses.homeposts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class SharedUser implements Parcelable {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getIs_shared() {
        return is_shared;
    }

    public void setIs_shared(Integer is_shared) {
        this.is_shared = is_shared;
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


    @SerializedName("id")
    private Integer id;

    @SerializedName("user_id")
    private Integer user_id;

    @SerializedName("content")
    private String content;

    @SerializedName("location")
    private String location;

    @SerializedName("is_shared")
    private Integer is_shared;

    @SerializedName("created_at")
    private String created_at;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    private String type;

    @SerializedName("user")
    private User user;

    public ArrayList<PostMedium> getPostMediumArrayList() {
        return postMediumArrayList;
    }

    public void setPostMediumArrayList(ArrayList<PostMedium> postMediumArrayList) {
        this.postMediumArrayList = postMediumArrayList;
    }

    @SerializedName("post_media")
    private ArrayList<PostMedium> postMediumArrayList;


    public SharedUser() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.user_id);
        dest.writeString(this.content);
        dest.writeString(this.location);
        dest.writeValue(this.is_shared);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.user, flags);
        dest.writeTypedList(this.postMediumArrayList);
        dest.writeString(this.type);
    }

    protected SharedUser(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.content = in.readString();
        this.location = in.readString();
        this.is_shared = (Integer) in.readValue(Integer.class.getClassLoader());
        this.created_at = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.postMediumArrayList = in.createTypedArrayList(PostMedium.CREATOR);
        this.type = in.readString();
    }

    public static final Creator<SharedUser> CREATOR = new Creator<SharedUser>() {
        @Override
        public SharedUser createFromParcel(Parcel source) {
            return new SharedUser(source);
        }

        @Override
        public SharedUser[] newArray(int size) {
            return new SharedUser[size];
        }
    };
}
