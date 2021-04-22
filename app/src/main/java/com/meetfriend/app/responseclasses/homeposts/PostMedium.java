
package com.meetfriend.app.responseclasses.homeposts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostMedium implements Parcelable {

    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("extension")
    private String mExtension;
    @SerializedName("file_name")
    private String mFileName;
    @SerializedName("file_path")
    private String mFilePath;
    @SerializedName("id")
    private Long mId;
    @SerializedName("posts_id")
    private Long mPostsId;
    @SerializedName("size")
    private String mSize;

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getExtension() {
        return mExtension;
    }

    public void setExtension(String extension) {
        mExtension = extension;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getPostsId() {
        return mPostsId;
    }

    public void setPostsId(Long postsId) {
        mPostsId = postsId;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCreatedAt);
        dest.writeString(this.mExtension);
        dest.writeString(this.mFileName);
        dest.writeString(this.mFilePath);
        dest.writeValue(this.mId);
        dest.writeValue(this.mPostsId);
        dest.writeString(this.mSize);
    }

    public PostMedium() {
    }

    protected PostMedium(Parcel in) {
        this.mCreatedAt = in.readString();
        this.mExtension = in.readString();
        this.mFileName = in.readString();
        this.mFilePath = in.readString();
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mPostsId = (Long) in.readValue(Long.class.getClassLoader());
        this.mSize = in.readString();
    }

    public static final Parcelable.Creator<PostMedium> CREATOR = new Parcelable.Creator<PostMedium>() {
        @Override
        public PostMedium createFromParcel(Parcel source) {
            return new PostMedium(source);
        }

        @Override
        public PostMedium[] newArray(int size) {
            return new PostMedium[size];
        }
    };
}
